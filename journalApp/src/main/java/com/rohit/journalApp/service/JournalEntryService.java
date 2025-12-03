package com.rohit.journalApp.service;

import com.rohit.journalApp.Entity.JournalEntry;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;


public interface JournalEntryService {
    void saveEntry(JournalEntry journalEntry, String userName);

    List<JournalEntry> getAll();

    Optional<JournalEntry> findById(ObjectId myId);

    boolean deleteById(ObjectId Id, String userName);

    JournalEntry updateEntryById(Optional<JournalEntry> myId, JournalEntry newupdatedEntry);
}
