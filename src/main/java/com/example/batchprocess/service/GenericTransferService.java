package com.example.batchprocess.service;


import com.example.batchprocess.config.MongoDBConfig;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class GenericTransferService {
    private final MongoDatabase mongoDatabase= MongoDBConfig.getDatabase("data");
    private final ObjectMapper mapper = new ObjectMapper();
    public  <T> void transferData(List<T> list, String collectionName) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        for (T entity : list) {

            try {
                String json = mapper.writeValueAsString(entity);
                Document doc = Document.parse(json);
                collection.insertOne(doc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Inserted " + list.size() + " entities into MongoDB collection: " + collectionName);
    }

}
