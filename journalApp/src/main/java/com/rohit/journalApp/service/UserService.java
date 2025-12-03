package com.rohit.journalApp.service;

import com.rohit.journalApp.Entity.User;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    void updateUser(User user);

    List<User> getAll();

    Optional<User> findById(ObjectId myId);

    boolean deleteById(ObjectId Id);


    Optional<User> findByUserName(String username);

    void deleteByUserName(String userName);

    void saveAdmin(User user);
}
