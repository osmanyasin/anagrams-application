package bsg.application.anagrams.controller;

import bsg.application.anagrams.core.Word;
import bsg.application.anagrams.core.WordMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/anagrams")
@RequiredArgsConstructor
@Slf4j
public class AnagramsController {

    private final WordMapper wordMapper;

    @Operation(summary = "Add a word", description = "Add a word to the list")
    @PostMapping(value = "/{word}")
    public void addNewWord(@PathVariable String word) {
        log.info("The word being added is {}", word);
    }

    @Operation(summary = "Get all words", description = "Retrieve a list of all words")
    @GetMapping(value = "/all")
    public List<String> retrieveAllWords() {
        log.info("Retrieving fine -> {}", "Mandem");
        return this.wordMapper.getAllWords().stream().map(Word::getContent).toList();
    }

    @Operation(summary = "Get anagrams", description = "Retrieve a list of anagrams for the word")
    @GetMapping(value = "/{word}")
    public List<String> retrieveAnagrams(@PathVariable String word) {
        log.info("Retrieving list of anagrams -> {} {}", word, "Anagrams gonna come from DB");
        return null;
    }

    @Operation(summary = "Get anagrams count summary", description = "Retrieve summary for the count of anagrams by word length")
    @GetMapping(value = "/summary")
    public String retrieveCountSummary() {
        return "Summary to be provided";
    }

    @Operation(summary = "Delete a word", description = "Delete a word from the list")
    @DeleteMapping(value = "/{word}")
    public void deleteWord(@PathVariable String word) {
        log.info("The word being deleted is {}", word);
    }
}
