package com.example.batchprocess.repository;

import com.example.batchprocess.entity.NotificationStickyMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationStickyMongoRepo extends MongoRepository<NotificationStickyMongo, String> {
}
