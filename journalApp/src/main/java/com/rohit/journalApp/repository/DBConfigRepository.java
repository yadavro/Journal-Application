package com.rohit.journalApp.repository;

import com.rohit.journalApp.Entity.DBConfig;
import com.rohit.journalApp.Entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBConfigRepository extends MongoRepository<DBConfig, ObjectId> {
}
