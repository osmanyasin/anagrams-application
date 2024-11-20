package bsg.application.anagrams.core;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@NoArgsConstructor
public class Word {
    private Long id;
    private String content;
    private int length;
    private String sortedKey;

    public Word(String content) {
        this.content = content;
        this.length = content.length();
        this.sortedKey = orderContent(content);
    }

    private static String orderContent(String content) {
        char[] chArr = content.toCharArray();
        Arrays.sort(chArr);
        return new String(chArr);
    }
}
