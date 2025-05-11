package com.andymur.projectw.controller;

import com.andymur.projectw.model.User;
import com.andymur.projectw.repository.UserRepository;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/api/spark")
public class SparkController {

    private final UserRepository userRepository;

    public SparkController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String runSparkJob() {
        run();
        return "Hey!";
    }

    private void run() {
        // Step 1: Initialize SparkSession
        SparkSession spark = SparkSession.builder()
                .appName("PostgresToSpark")
                .config("spark.master", "local[*]") // Local mode
                .config("spark.storage.memoryMapThreshold", "0")
                .config("spark.ui.enabled", "false") // Disable Spark UI
                .config("spark.metrics.conf.*.sink.servlet.enabled", "false") // Disable metrics servlet
                .getOrCreate();

        // Step 2: PostgreSQL JDBC connection properties
        String jdbcUrl = "jdbc:postgresql://localhost:5432/mydb";
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "myuser");
        connectionProperties.put("password", "mypassword");
        connectionProperties.put("driver", "org.postgresql.Driver");

        Map<String, String> connectionPropertiesMap = new HashMap<>();
        connectionPropertiesMap.put("user", "myuser");
        connectionPropertiesMap.put("password", "mypassword");
        connectionPropertiesMap.put("driver", "org.postgresql.Driver");

        String tableName = "users";

        // Step 3: Properly parenthesized query
        String query = "(select * from users)";

        // Step 4: Load data from PostgreSQL into DataFrame
        /*Dataset<Row> df = spark.read()
                .jdbc(jdbcUrl, query, connectionProperties);*/

        Dataset<Row> df = spark.createDataFrame(userRepository.findAll(), User.class);

        // Step 5: Show the loaded data
        df.show();

        // Step 6: Perform SQL operations
        df.createOrReplaceTempView("users"); // Create temporary view
        Dataset<Row> sqlResult = spark.sql("SELECT name FROM users WHERE name = 'Alice'");
        sqlResult.show();

        Dataset<Row> usersDF = spark.read()
                .format("jdbc")
                .option("url", jdbcUrl)
                .option("dbtable", tableName)
                .options(connectionPropertiesMap)
                .load();

        usersDF.show();

        // Step 7: Stop SparkSession
        spark.stop();
    }

}
