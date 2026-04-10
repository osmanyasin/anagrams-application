package com.bsg_selectathon.anagrams_service.dto;

import java.util.Map;

public record AnagramCountResponse(
        Map<Integer, Long> countsByLength,
        long elapsedMs
) {}
