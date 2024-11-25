package bsg.application.anagrams.core;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordMapper wordMapper;

    @CacheEvict(value = "paginatedWordsCache", allEntries = true)
    public void addWord(String word) {
        this.wordMapper.save(new Word(word));
    }

    public void addWords(List<String> words) {
        List<Word> wordRecords = words.stream().map(Word::new).toList();
        this.wordMapper.saveAll(wordRecords);
    }

    public List<String> getAllWords() {
        return this.wordMapper.findAll();
    }

    @Cacheable(value = "paginatedWordsCache", key = "'limit_' + #limit + '_offset_' + #offset")
    public List<String> getPaginatedWords(int limit, int offset) {
        return this.wordMapper.findPaginated(limit, offset);
    }

    public List<String> getAnagrams(String word) {
        String sortedKey = genSortedKey(word);
        return this.wordMapper.findAnagrams(sortedKey, word);
    }

    private static String genSortedKey(String content) {
        char[] chArr = content.toCharArray();
        Arrays.sort(chArr);
        return new String(chArr);
    }

    public List<AnagramCount> getAnagramCountByLength() {
        return this.wordMapper.findAnagramCountsByLength();
    }

    @CacheEvict(value = "paginatedWordsCache", allEntries = true)
    public void removeWord(String word) {
        this.wordMapper.remove(word);
    }
}
