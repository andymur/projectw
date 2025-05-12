package com.andymur.projectw.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GramServiceTest {
    @Test
    void generateBiGrams() {
        GramService gramService = new GramService();
        List<String> bigrams = gramService.createNGram("hello", 2);
        assertEquals(4, bigrams.size());
        assertEquals(List.of("he", "el", "ll", "lo"), bigrams);
    }
}