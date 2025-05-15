package com.andymur.projectw.service;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import java.util.ArrayList;
import java.util.List;

import static com.andymur.projectw.service.TfidfVectorizer.computeCosineSimilarity;

public class DBSCANService {

    private final DBSCANClusterer<Document> clusterer;

    public DBSCANService(double eps, int minPts) {
        clusterer = new DBSCANClusterer<>(eps, minPts, new CosineDistanceMeasure());
    }

    private List<Document> createClusterableDocuments(List<String> documents) {
        TfidfVectorizer vectorizer = new TfidfVectorizer();
        List<double[]> tfidfVectors = vectorizer.fitTransform(documents);
        List<Document> documentList = new ArrayList<>(tfidfVectors.size());

        for (int i = 0; i < tfidfVectors.size(); i++) {
            documentList.add(new Document(documents.get(i), tfidfVectors.get(i)));
        }
        printCosineSimilarity(documentList);
        return documentList;
    }

    private void printCosineSimilarity(List<Document> documents) {
        for (int i = 0; i < documents.size() - 1; i++) {
            for (int j = i + 1; j < documents.size(); j++) {
                System.out.printf("Cosine similarity between %s & %s is: %f%n", documents.get(i).getContent(),
                        documents.get(j).getContent(),
                        computeCosineSimilarity(documents.get(i).getPoint(), documents.get(j).getPoint())
                );
            }
        }
    }

    public void clusterAndPrintResults(List<String> documents) {
        List<Cluster<Document>> clusters = clusterer.cluster(createClusterableDocuments(documents));

        int clusterNum = 1;
        System.out.println("Found " + clusters.size() + " clusters:");
        for (Cluster<Document> cluster : clusters) {
            System.out.println("Cluster " + clusterNum++ + ":");
            for (Document document : cluster.getPoints()) {
                System.out.println("  " + document.getContent());
            }
        }
    }

    public static class Document implements Clusterable {

        private final String content;
        private double[] point;

        public Document(String content) {
            this.content = content;
        }

        public Document(String content, double[] point) {
            this.content = content;
            this.point = point;
        }

        public String getContent() {
            return content;
        }

        public void setPoint(double[] point) {
            this.point = point;
        }

        @Override
        public double[] getPoint() {
            return point;
        }
    }

    public static class CosineDistanceMeasure implements DistanceMeasure {

        @Override
        public double compute(double[] a, double[] b) {
            double dotProduct = 0.0;
            double normA = 0.0;
            double normB = 0.0;

            for (int i = 0; i < a.length; i++) {
                dotProduct += a[i] * b[i];
                normA += a[i] * a[i];
                normB += b[i] * b[i];
            }

            if (normA == 0.0 || normB == 0.0) {
                return 1.0; // maximum distance if vectors are zero vectors
            }

            double cosineSimilarity = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
            return 1.0 - cosineSimilarity; // cosine distance
        }
    }
}
