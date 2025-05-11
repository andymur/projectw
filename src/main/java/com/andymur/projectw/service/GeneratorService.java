package com.andymur.projectw.service;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashSet;
import java.util.Set;

public class GeneratorService {

    public static final int DEFAULT_TOKEN_SIZE = 6;

    public Set<String> generateNames() {
        return Set.of();
    }

    public Set<String> generateTokens(int nTokens) {
        int i = 0;
        Set<String> tokens = new HashSet<>();
        RandomStringUtils insecureStringUtil = RandomStringUtils.insecure();
        while (i < nTokens) {
            if(tokens.add(insecureStringUtil.nextAlphanumeric(DEFAULT_TOKEN_SIZE))) {
                i++;
            }
        }
        return tokens;
    }
}
