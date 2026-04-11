package com.bsg_selectathon.anagrams_service.service;

import com.bsg_selectathon.anagrams_service.dto.AnagramCountResponse;
import com.bsg_selectathon.anagrams_service.dto.AnagramCountRow;
import com.bsg_selectathon.anagrams_service.dto.AnagramsResponse;
import com.bsg_selectathon.anagrams_service.entity.Word;
import com.bsg_selectathon.anagrams_service.repository.WordRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final EntityManager entityManager;

    private static String sorted(String word) {
        char[] chars = word.toLowerCase().toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public long wordCount() {
        return wordRepository.count();
    }

    @Transactional
    public void bulkInsert(List<String> words) {
        List<Word> entities = words.stream()
                .filter(w -> w != null && !w.isBlank())
                .map(String::trim)
                .map(w -> new Word(w, sorted(w)))
                .toList();

        if (entities.isEmpty()) return;

        int chunkSize = 500;
        for (int i = 0; i < entities.size(); i += chunkSize) {
            List<Word> chunk = entities.subList(i, Math.min(i + chunkSize, entities.size()));
            wordRepository.saveAll(chunk);
            wordRepository.flush();
            entityManager.clear();
        }
    }

    @Transactional
    public void addWords(List<String> words) {
        Set<String> uniqueInputs = words.stream()
                .filter(w -> w != null && !w.isBlank())
                .map(String::trim)
                .collect(Collectors.toSet());

        if (uniqueInputs.isEmpty()) return;

        Set<String> existingWords = wordRepository.findAllByOriginalIgnoreCaseIn(new ArrayList<>(uniqueInputs))
                .stream()
                .map(w -> w.getOriginal().toLowerCase())
                .collect(Collectors.toSet());

        List<Word> toInsert = uniqueInputs.stream()
                .filter(w -> !existingWords.contains(w.toLowerCase()))
                .map(w -> new Word(w, sorted(w)))
                .toList();

        if (!toInsert.isEmpty()) {
            wordRepository.saveAll(toInsert);
        }
    }

    @Transactional
    public void deleteWords(List<String> words) {
        List<String> normalised = words.stream()
                .filter(w -> w != null && !w.isBlank())
                .map(String::trim)
                .toList();

        if (normalised.isEmpty()) return;

        wordRepository.deleteAllByOriginalIgnoreCaseIn(normalised);
    }

    public Page<String> getAllWords(Pageable pageable) {
        return wordRepository.findAll(pageable).map(Word::getOriginal);
    }

    public AnagramsResponse getAnagrams(String word) {
        List<String> anagrams = wordRepository.findBySorted(sorted(word))
                .stream()
                .map(Word::getOriginal)
                .filter(w -> !w.equalsIgnoreCase(word))
                .toList();

        return new AnagramsResponse(word, anagrams);
    }

    public AnagramCountResponse getAnagramCountByLength() {
        StopWatch timer = new StopWatch();
        timer.start();

        Map<Integer, Long> countsByLength = wordRepository.countAnagramGroupsByWordLength()
                .stream()
                .collect(Collectors.toMap(
                        AnagramCountRow::wordLength,
                        AnagramCountRow::groupCount,
                        (a, _) -> a,
                        LinkedHashMap::new
                ));

        timer.stop();

        return new AnagramCountResponse(countsByLength, timer.getTotalTimeMillis());
    }
}
