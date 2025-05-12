package com.andymur.projectw.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EncoderServiceTest {

    @Test
    public void testEncode() {
        DictionaryService dictionaryService = new DictionaryService();
        GramService gramService = new GramService();

        // he, el, ll, lo
        List<String> helloGrams = gramService.createNGram("hello", 2);
        // ye, el, ll, lo, ow
        List<String> yellowGrams = gramService.createNGram("yellow", 2);

        List<String> fellowGrams = gramService.createNGram("fellow", 2);

        // in total: he, el, ll, lo, ye, ow
        Stream.concat(helloGrams.stream(), yellowGrams.stream()).forEach(
                dictionaryService::addNGram
        );

        fellowGrams.forEach(dictionaryService::addNGram);

        EncoderService encoderService = new EncoderService(dictionaryService);
        assertEquals(0b0001111, encoderService.encode("hello"));
        assertEquals(0b0111110, encoderService.encode("yellow"));
        assertEquals(0b1101110, encoderService.encode("fellow"));
    }
}