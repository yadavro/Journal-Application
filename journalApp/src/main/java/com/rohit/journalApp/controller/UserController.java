package com.rohit.journalApp.controller;

import com.rohit.journalApp.Entity.User;
import com.rohit.journalApp.Entity.Wether;
import com.rohit.journalApp.service.UserService;
import com.rohit.journalApp.service.imp.WetherServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private WetherServiceImp wetherServiceImp;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Optional<User> userInDB = userService.findByUserName(username);
        if (userInDB.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with username: " + username);
        }
        User existingUser = userInDB.get();
        existingUser.setUserName(user.getUserName());
        existingUser.setPassword(user.getPassword());

        User updatedUser = userService.saveUser(existingUser);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByUserName() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        userService.deleteByUserName(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<?> getCurrent(@RequestParam String city) {
        Wether wether_report = wetherServiceImp.getReport(city);
        if (wether_report != null) {
            return ResponseEntity.status(HttpStatus.OK).body(wether_report);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Something went wrong!");
    }


}
