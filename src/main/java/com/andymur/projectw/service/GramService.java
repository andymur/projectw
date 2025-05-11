package com.andymur.projectw.service;

import java.util.ArrayList;
import java.util.List;

public class GramService {

    public List<String> createNGram(String source, int n) {
        List<String> ngrams = new ArrayList<>();
        if (source == null || source.length() < n) {
            return ngrams;
        }

        for (int i = 0; i <= source.length() - n; i++) {
            ngrams.add(source.substring(i, i + n));
        }

        return ngrams;
    }
}
