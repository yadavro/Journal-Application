package com.rohit.journalApp.Entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "DBConfig")
public class DBConfig {
    @Id
    private ObjectId id;

    private String key;
    private String value;
}
