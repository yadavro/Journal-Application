package com.rohit.journalApp.controller;

import com.rohit.journalApp.Entity.JournalEntry;
import com.rohit.journalApp.Entity.User;
import com.rohit.journalApp.service.JournalEntryService;
import com.rohit.journalApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@Slf4j
public class JournalController {
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAll() {
        log.info("Inside get all entries by user");
        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Optional<User> user = userService.findByUserName(userName);
        User currentUser = user.get();
        log.info("user details:"+currentUser.getJournalEntries());
        List<JournalEntry> allEntryByCurrentUser = currentUser.getJournalEntries();
        if (allEntryByCurrentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("exiting the get-all-entries by user");
        return ResponseEntity.status(200).body(allEntryByCurrentUser);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry) {
        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        try {
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(journalEntry, userName);
            return ResponseEntity.status(HttpStatus.CREATED).body(journalEntry);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Optional<User> users = userService.findByUserName(userName);
        User user = users.get();
        List<JournalEntry> entries = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if (!entries.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if (journalEntry.isPresent()) {
                return ResponseEntity.ok(journalEntry.get());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId) {
        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        if (journalEntry.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal Entry not found! Oops");
        }
        journalEntryService.deleteById(myId, userName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Journal Entry deleted successfully, Hurrah!");
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable ObjectId myId, @RequestBody JournalEntry newupdatedEntry) {
        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        JournalEntry getEntryById = getJournalEntryById(myId).getBody();
        if (getEntryById == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        JournalEntry updatedEntry = journalEntryService.updateEntryById(Optional.of(getEntryById), newupdatedEntry);
        return ResponseEntity.status(200).body(Optional.ofNullable(updatedEntry));
    }
}
