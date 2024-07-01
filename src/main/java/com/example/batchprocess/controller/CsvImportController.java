package com.example.batchprocess.controller;

import com.example.batchprocess.config.MongoDBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/csv")
public class CsvImportController {

    private final MongoDBConfig mongoDBConfig;

    @Autowired
    public CsvImportController(MongoDBConfig mongoDBConfig) {
        this.mongoDBConfig = mongoDBConfig;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsvToMongoDB(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a CSV file.");
        }

        try {
            Resource csvFileResource = file.getResource();
            mongoDBConfig.importCsvToMongoDB(csvFileResource, "notification_sticky");
            return ResponseEntity.ok().body("CSV file imported successfully to MongoDB.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import CSV file: " + e.getMessage());
        }
    }
}
