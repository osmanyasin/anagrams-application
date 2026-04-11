package com.bsg_selectathon.anagrams_service.startup;

import com.bsg_selectathon.anagrams_service.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DictionarySeeder implements ApplicationRunner {

    private final WordService wordService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("DictionarySeeder triggered - checking word count...");

        if (wordService.wordCount() > 0) {
            log.info("Dictionary table already populated ({} words) - skipping seed.", wordService.wordCount());
            return;
        }

        ClassPathResource resource = new ClassPathResource("Dictionary.txt");

        if (!resource.exists()) {
            log.warn("Dictionary.txt not found on classpath - skipping seed load.");
            return;
        }

        StopWatch timer = new StopWatch("Dictionary Seed");

        timer.start("read");
        List<String> words = new ArrayList<>(200_000);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    words.add(trimmed);
                }
            }
        }
        timer.stop();

        timer.start("insert");
        int chunkSize = 500;
        for (int i = 0; i < words.size(); i += chunkSize) {
            List<String> chunk = words.subList(i, Math.min(i + chunkSize, words.size()));
            wordService.bulkInsert(chunk);
        }
        timer.stop();

        log.info(timer.prettyPrint());
    }
}