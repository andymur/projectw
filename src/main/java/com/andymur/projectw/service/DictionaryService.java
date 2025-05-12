package com.andymur.projectw.service;

import java.util.*;

public class DictionaryService {
    private final Set<String> nGrams = new LinkedHashSet<>();
    private final Map<String, Long> dictionary = new HashMap<>();

    private int dictSize = 0;

    public boolean addNGram(String nGram) {
        if (nGrams.add(nGram)) {
            dictionary.put(nGram, 1L << dictSize);
            dictSize++;
            return true;
        }
        return false;
    }

    public Long lookupNGram(String nGram) {
        return dictionary.get(nGram);
    }

    public int size() {
        return dictionary.size();
    }

}
