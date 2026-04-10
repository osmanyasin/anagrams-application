package com.bsg_selectathon.anagrams_service.dto;

public record AnagramCountRow(
        Integer wordLength,
        Long groupCount
) {}
