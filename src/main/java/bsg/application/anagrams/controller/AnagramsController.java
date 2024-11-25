package bsg.application.anagrams.controller;

import bsg.application.anagrams.core.AnagramCount;
import bsg.application.anagrams.core.WordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/anagrams")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class AnagramsController {

    private final WordService wordService;

    @Operation(summary = "Add a word", description = "Add a word to the list")
    @PostMapping(value = "/{word}")
    public ResponseEntity<Void> addNewWord(@PathVariable String word) {
        log.info("The word being added is {}", word);
        this.wordService.addWord(word.toUpperCase());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all words", description = "Retrieve a list of all words")
    @GetMapping(value = "/all")
    public ResponseEntity<List<String>> retrieveAllWords() {
        log.info("Retrieving fine -> {}", "Mandem");
        List<String> allWords = this.wordService.getAllWords();
        return ResponseEntity.ok(allWords);
    }

    @Operation(summary = "Get words", description = "Retrieve a paginated list of words")
    @GetMapping(value = "/words")
    public ResponseEntity<List<String>> retrieveWords(
            @RequestParam("limit") int limit,
            @RequestParam("offset") int offset
        ) {
        log.info("Retrieving fine paginated -> {}", "Mandem");
        List<String> paginatedWords = this.wordService.getPaginatedWords(limit, offset);
        return ResponseEntity.ok(paginatedWords);
    }

    @Operation(summary = "Get anagrams", description = "Retrieve a list of anagrams for the word")
    @GetMapping(value = "/{word}")
    public ResponseEntity<List<String>> retrieveAnagrams(@PathVariable String word) {
        log.info("Retrieving list of anagrams -> {} {}", word, "Anagrams gonna come from DB");
        List<String> anagrams = this.wordService.getAnagrams(word.toUpperCase());
        return ResponseEntity.ok(anagrams);
    }

    @Operation(summary = "Get anagrams count summary", description = "Retrieve summary for the count of anagrams by word length")
    @GetMapping(value = "/summary")
    public ResponseEntity<List<AnagramCount>> retrieveAnagramsCount() {
        List<AnagramCount> anagramCounts = this.wordService.getAnagramCountByLength();
        return ResponseEntity.ok(anagramCounts);
    }

    @Operation(summary = "Delete a word", description = "Delete a word from the list")
    @DeleteMapping(value = "/{word}")
    public ResponseEntity<Void> deleteWord(@PathVariable String word) {
        log.info("The word being deleted is {}", word);
        this.wordService.removeWord(word.toUpperCase());
        return ResponseEntity.ok().build();
    }
}
