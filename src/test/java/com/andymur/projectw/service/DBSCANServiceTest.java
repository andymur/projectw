package com.andymur.projectw.service;

import org.junit.jupiter.api.Test;

import java.util.List;

class DBSCANServiceTest {

    @Test
    public void testClusterer() {
        DBSCANService dbscanService = new DBSCANService(0.7, 2);
        dbscanService.clusterAndPrintResults(
                List.of(
                    "Amzon",
                    "Microsoft",
                    "Amazon corp",
                    "Amazon corporation",
                    "Microsoft Inc",
                    "Microsoft America"
                )
        );
    }
}