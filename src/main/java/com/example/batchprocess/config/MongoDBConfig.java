package com.example.batchprocess.config;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoDBConfig {

    private static final String connectionString = "mongodb://localhost:27017";
    private final MongoDatabase mongoDatabase = MongoDBConfig.getDatabase("data");

    public static MongoClient getMongoClient() {
        return MongoClients.create(new ConnectionString(connectionString));
    }

    public static MongoDatabase getDatabase(String dbName) {
        return getMongoClient().getDatabase(dbName);
    }

    public void importCsvToMongoDB(Resource csvFileResource, String collectionName) throws IOException {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFileResource.getInputStream()))) {

            List<Document> documentList = new ArrayList<>();String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                Document doc = new Document()
                        .append("uuid", fields[0])
                        .append("id", fields[1])
                        .append("type", fields[2])
                        .append("nsType", fields[3])
                        .append("userId", fields[4])
                        .append("transId", fields[5])
                        .append("requestTransId", fields[6])
                        .append("title", fields[7])
                        .append("message", fields[8])
                        .append("urlDetail", fields[9])
                        .append("notificationId", fields[10])
                        .append("startTime", LocalDateTime.parse(fields[11]))
                        .append("endTime", LocalDateTime.parse(fields[12]))
                        .append("status", fields[13])
                        .append("viewStatus", fields[14])
                        .append("priority", Integer.parseInt(fields[15]))
                        .append("productCategoryCode", fields[16])
                        .append("objectTypeDesc", fields[17])
                        .append("objectValueDesc", fields[18])
                        .append("goValue", fields[19])
                        .append("description", fields[20])
                        .append("productId", fields[21])
                        .append("productCode", fields[22])
                        .append("shortContent", fields[23])
                        .append("thanked", fields[24]);
                documentList.add(doc);
                if (documentList.size()==1000){
                    collection.insertMany(documentList);
                    documentList.clear();
                }
            }
            collection.insertMany(documentList); /* Thêm nốt các phần ử cuối cùng */
        }
    }
}
