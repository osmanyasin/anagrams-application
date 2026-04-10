package com.bsg_selectathon.anagrams_service.dto;

import java.util.List;

public record AnagramsResponse(
        String word,
        List<String> anagrams
) {}
