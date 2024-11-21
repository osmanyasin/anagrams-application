package bsg.application.anagrams.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordMapper wordMapper;

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

    public void removeWord(String word) {
        this.wordMapper.remove(word);
    }
}
