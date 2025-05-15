package com.andymur.projectw.service;

import java.util.*;
import java.util.stream.Collectors;

public class TfidfVectorizer {

    private final Map<String, Integer> vocabulary = new HashMap<>();
    private final Map<String, Double> idfScores = new HashMap<>();

    // Fit the vectorizer to documents
    public void fit(List<String> documents) {
        Set<String> allTerms = new HashSet<>();
        List<Set<String>> docTerms = new ArrayList<>();

        for (String doc : documents) {
            Set<String> tokens = tokenize(doc);
            docTerms.add(tokens);
            allTerms.addAll(tokens);
        }

        int index = 0;
        for (String term : allTerms) {
            vocabulary.put(term, index++);
        }

        // Compute IDF
        int totalDocs = documents.size();
        for (String term : vocabulary.keySet()) {
            int docCount = 0;
            for (Set<String> doc : docTerms) {
                if (doc.contains(term)) {
                    docCount++;
                }
            }
            double idf = Math.log((double) totalDocs / (1 + docCount)) + 1.0; // Smoothing as in scikit-learn
            idfScores.put(term, idf);
        }
    }

    // Transform documents into TF-IDF vectors
    public List<double[]> transform(List<String> documents) {
        List<double[]> tfidfVectors = new ArrayList<>();

        for (String doc : documents) {
            double[] vector = new double[vocabulary.size()];
            Map<String, Integer> termCounts = new HashMap<>();

            List<String> tokens = new ArrayList<>(tokenize(doc));
            for (String token : tokens) {
                termCounts.put(token, termCounts.getOrDefault(token, 0) + 1);
            }

            for (String term : termCounts.keySet()) {
                if (vocabulary.containsKey(term)) {
                    int index = vocabulary.get(term);
                    double tf = (double) termCounts.get(term) / tokens.size();
                    double idf = idfScores.get(term);
                    vector[index] = tf * idf;
                }
            }

            tfidfVectors.add(vector);
        }

        return tfidfVectors;
    }

    public List<double[]> fitTransform(List<String> documents) {
        fit(documents);
        return transform(documents);
    }

    private Set<String> tokenize(String document) {
        return Arrays.stream(document.toLowerCase().split("\\W+"))
                .filter(token -> !token.isBlank())
                .collect(Collectors.toSet());
    }

    // Optional: Get feature names
    public List<String> getFeatureNames() {
        return vocabulary.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static double computeCosineSimilarity(double[] vecA, double[] vecB) {
        if (vecA.length != vecB.length) {
            throw new IllegalArgumentException("Vectors must be of same length");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vecA.length; i++) {
            dotProduct += vecA[i] * vecB[i];
            normA += Math.pow(vecA[i], 2);
            normB += Math.pow(vecB[i], 2);
        }

        if (normA == 0.0 || normB == 0.0) {
            return 0.0; // avoid division by zero
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public static void main(String[] args) {
        List<String> docs = Arrays.asList(
                "The quick brown fox",
                "The quick brown dog",
                "The lazy dog"
        );

        TfidfVectorizer vectorizer = new TfidfVectorizer();
        List<double[]> tfidfVectors = vectorizer.fitTransform(docs);

        List<String> features = vectorizer.getFeatureNames();

        System.out.println("Features: " + features);
        for (double[] vec : tfidfVectors) {
            System.out.println(Arrays.toString(vec));
        }

        for (int i = 0; i < docs.size() - 1; i++) {
            for (int j = i + 1; j < docs.size(); j++) {
                System.out.printf("Cosine similarity between %s & %s is: %f%n", docs.get(i), docs.get(j),
                            computeCosineSimilarity(tfidfVectors.get(i), tfidfVectors.get(j))
                        );
            }
        }
    }
}

