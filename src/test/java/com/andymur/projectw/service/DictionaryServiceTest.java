package com.andymur.projectw.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DictionaryServiceTest {

    @Test
    public void testShouldBuildDictionary() {
        DictionaryService dictionaryService = new DictionaryService();
        GramService gramService = new GramService();

        // he, el, ll, lo
        List<String> helloGrams = gramService.createNGram("hello", 2);
        // ye, el, ll, lo, ow
        List<String> yellowGrams = gramService.createNGram("yellow", 2);

        // in total: he, el, ll, lo, ye, ow
        Stream.concat(helloGrams.stream(), yellowGrams.stream()).forEach(
                dictionaryService::addNGram
        );

        assertEquals(6, dictionaryService.size());
        assertEquals(0b1, dictionaryService.lookupNGram("he"));
        assertEquals(0b10, dictionaryService.lookupNGram("el"));
        assertEquals(0b100, dictionaryService.lookupNGram("ll"));
        assertEquals(0b1000, dictionaryService.lookupNGram("lo"));
        assertEquals(0b10000, dictionaryService.lookupNGram("ye"));
        assertEquals(0b100000, dictionaryService.lookupNGram("ow"));
    }
}