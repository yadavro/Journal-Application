package com.rohit.journalApp.repository.imp;

import com.rohit.journalApp.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class UserMongoTemplateRepoImp {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserWithSA(){
        Query query=new Query();
        query.addCriteria(
                Criteria.where("email")
                        .regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        );

        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        return mongoTemplate.find(query, User.class);
    }
}
