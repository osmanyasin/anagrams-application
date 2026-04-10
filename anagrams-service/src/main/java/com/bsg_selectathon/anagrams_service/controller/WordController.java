package com.bsg_selectathon.anagrams_service.controller;

import com.bsg_selectathon.anagrams_service.dto.AnagramCountResponse;
import com.bsg_selectathon.anagrams_service.dto.AnagramsResponse;
import com.bsg_selectathon.anagrams_service.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Anagrams Service", description = "Manage a dictionary of words and compute anagrams")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @Operation(summary = "Add words to the dictionary",
            description = "Accepts a JSON body with a list of words and adds them to the in-memory dictionary.")
    @ApiResponse(responseCode = "201", description = "Word added successfully")
    @ApiResponse(responseCode = "400", description = "Request body is missing or malformed", content = @Content)
    @PostMapping(value = "/words", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addWords(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of words to add",
                    required = true,
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(type = "string")),
                            examples = @ExampleObject(value = """
                                    ["listen", "silent", "enlist", "hello"]
                                    """)))
            @RequestBody List<String> words
    ) {
        wordService.addWords(words);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Delete words from the dictionary",
            description = "Removes the specified words from the dictionary. Returns 204 whether or not the words existed.")
    @ApiResponse(responseCode = "204", description = "Words deleted (or were not present)")
    @ApiResponse(responseCode = "400", description = "Request body is missing or empty", content = @Content)
    @DeleteMapping("/words")
    public ResponseEntity<Void> deleteWords(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of words to delete",
                    required = true,
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(type = "string")),
                            examples = @ExampleObject(value = """
                                ["listen", "silent", "enlist"]
                                """)))
            @RequestBody List<String> words
    ) {
        wordService.deleteWords(words);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all words in the dictionary",
            description = "Returns the complete list of words currently stored in the dictionary.")
    @ApiResponse(responseCode = "200", description = "Word returned successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(type = "string")),
                    examples = @ExampleObject(value = """
                            ["listen", "silent", "enlist", "hello"]
                            """)))
    @GetMapping(value = "/words", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<String>> getAllWords(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "50")
            @RequestParam(defaultValue = "50") int size,

            @Parameter(description = "Sort field and direction", example = "original,asc")
            @RequestParam(defaultValue = "original,asc") String sort
    ) {
        String[] sortParts = sort.split(",");
        Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParts[0]));
        return ResponseEntity.ok(wordService.getAllWords(pageable));
    }

    @Operation(summary = "Find anagrams for a word",
            description = """
                    Returns all words in the dictionary that are anagrams of the supplied word.
                    The word itself is excluded from the results.
                    """)
    @ApiResponse(responseCode = "200", description = "Anagrams found (may be an empty list)",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AnagramsResponse.class),
                    examples = @ExampleObject(value = """
                            { "word": "listen", "anagrams": ["silent", "enlist"] }
                            """)))
    @ApiResponse(responseCode = "400", description = "Word path variable is blank", content = @Content)
    @GetMapping(value = "/anagrams/{word}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnagramsResponse> getAnagrams(
            @Parameter(description = "The word to find anagrams for", example = "listen", required = true)
            @PathVariable String word
    ) {
        return ResponseEntity.ok(wordService.getAnagrams(word));
    }

    @Operation(summary = "Count anagrams grouped by word length",
            description = """
                    Scans the entire dictionary, groups words that are anagrams of each other,
                    and returns the count of distinct anagram groups per word length.
                    """)
    @ApiResponse(responseCode = "200", description = "Counts returned successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AnagramCountResponse.class),
                    examples = @ExampleObject(value = """
                            { "countsByLength": { "6": 1, "5": 0 } }
                            """)))
    @GetMapping(value = "/anagrams/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnagramCountResponse> getAnagramCountByLength() {
        return ResponseEntity.ok(wordService.getAnagramCountByLength());
    }
}
