package com.andymur.projectw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class GeneratorServiceTest {

    @Test
    void generateTokens() {
        GeneratorService generatorService = new GeneratorService();
        Set<String> tokens = generatorService.generateTokens(1000_000);
        Assertions.assertEquals(1000_000, tokens.size());
    }
}