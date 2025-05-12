package com.andymur.projectw.service;

import java.util.List;

public class EncoderService {

    private final DictionaryService dictionaryService;
    private final GramService gramService = new GramService();

    public EncoderService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    long encode(String input) {
        List<String> nGrams = gramService.createNGram(input, 2);
        long result = 0;
        for (String nGram : nGrams) {
            result |= dictionaryService.lookupNGram(nGram);
        }
        return result;
    }
}
