package bsg.application.anagrams;

import bsg.application.anagrams.core.Word;
import bsg.application.anagrams.core.WordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private static final int CHUNK_SIZE = 10_000;
    private final WordMapper wordMapper;

    @Override
    public void run(String... args) throws Exception {
        try (Stream<String> lines = Files.lines(Paths.get("/tmp/Dictionary.txt"))) {
            AtomicLong totalInserts = new AtomicLong();
            List<Word> wordRecords = new ArrayList<>(CHUNK_SIZE);
            lines.forEach(word -> {
                wordRecords.add(new Word(word));
                if (wordRecords.size() == CHUNK_SIZE) {
                    insertBatch(wordRecords);
                    totalInserts.addAndGet(wordRecords.size());
                    wordRecords.clear();
                }
            });

            if (!wordRecords.isEmpty()) {
                insertBatch(wordRecords);
                totalInserts.addAndGet(wordRecords.size());
                wordRecords.clear();
            }

            log.info("Inserted a total of {} words", totalInserts);
        } catch (IOException e) {
            log.error("Error reading dictionary file", e);
        }
    }

    @Transactional
    private void insertBatch(List<Word> wordRecords) {
        try {
            this.wordMapper.insertWords(wordRecords);
            log.debug("Inserted {} words", wordRecords.size());
        } catch (Exception e) {
            log.error("Error inserting batch", e);
        }
    }
}
