package com.rohit.journalApp.controller;

import com.rohit.journalApp.Entity.User;
import com.rohit.journalApp.config.AppCache;
import com.rohit.journalApp.repository.imp.UserMongoTemplateRepoImp;
import com.rohit.journalApp.service.UserService;
import com.rohit.journalApp.service.imp.EmailServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserMongoTemplateRepoImp userMongoTemplateRepoImp;

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private EmailServiceImp emailServiceImp;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");


    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAll();
        if (users != null && !users.isEmpty()) {
            return ResponseEntity.status(200).body(users);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/create-admin-user")
    public void createAdminUser(@RequestBody User user) {
        userService.saveAdmin(user);
    }

    @GetMapping("/db-config")
    public ResponseEntity<?> getAllDBCOnfig() {
        appCache.init();
        log.info("db-congif");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/sentiment")
    public ResponseEntity<?> getSentiment() {
        List<User> user = userMongoTemplateRepoImp.getUserWithSA();
        log.info("list of user: " + user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/sent-mail")
    public void sendMail(@RequestParam String body, @RequestParam String subject) {
        List<User> allUsers = userService.getAll();
        List<String> validEmails = allUsers.stream()
                .map(User::getEmail)
                .filter(Objects::nonNull)
                .filter(email -> EMAIL_PATTERN.matcher(email).matches())
                .collect(Collectors.toList());
        if (validEmails.isEmpty()) {
            log.error("No valid emails found. Aborting sendMail.");
            return;
        }
        emailServiceImp.sendMail(validEmails, body, subject);
    }


}
