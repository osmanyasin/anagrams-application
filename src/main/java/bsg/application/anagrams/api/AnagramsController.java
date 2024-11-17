package bsg.application.anagrams.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/anagrams")
@Slf4j
public class AnagramsController {
    @Operation(summary = "Add a word", description = "Add a word to the list")
    @PostMapping(value = "/{word}")
    public void addNewWord(@PathVariable String word) {
        log.info("The word being added is {}", word);
    }

    @Operation(summary = "Get all words", description = "Retrieve a list of all words")
    @GetMapping(value = "/all")
    public List<String> retrieveAllWords() {
        log.info("Retrieving fine -> {}", "Mandem");
        return List.of("Ozzie", "Abdi", "Said", "Ab Moe", "JJ", "Dalmar", "Hamaam");
    }

    @Operation(summary = "Get anagrams", description = "Retrieve a list of anagrams for the word")
    @GetMapping(value = "/{word}")
    public List<String> retrieveAnagrams(@PathVariable String word) {
        log.info("Retrieving list of anagrams -> {} {}", word, "Anagrams gonna come from DB");
        return List.of("Ozzie", "Abdi", "Said", "Ab Moe", "JJ", "Dalmar", "Hamaam");
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
