package com.rohit.journalApp.service.imp;

import com.rohit.journalApp.Entity.JournalEntry;
import com.rohit.journalApp.Entity.User;
import com.rohit.journalApp.repository.JournalEntryRepository;
import com.rohit.journalApp.service.JournalEntryService;
import com.rohit.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryServiceImp implements JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
        Optional<User> user = userService.findByUserName(userName);
        User currentUser = user.get();
        currentUser.getJournalEntries().add(savedEntry);
        userService.updateUser(currentUser);
    }

    @Override
    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    @Override
    public Optional<JournalEntry> findById(ObjectId myId) {
        return journalEntryRepository.findById(myId);
    }

    @Override
    @Transactional
    public boolean deleteById(ObjectId Id, String userName) {

        try {
            Optional<User> user = userService.findByUserName(userName);
            User currentUser = user.get();

            boolean removed = currentUser.getJournalEntries().removeIf(entry -> entry.getId().equals(Id));
            if (removed) {
                userService.updateUser(currentUser);
                journalEntryRepository.deleteById(Id);
                return true;
            }
        } catch (Exception e) {
            System.out.println("error occcured during delettion!");
        }
        return false;
    }

    @Override
    public JournalEntry updateEntryById(Optional<JournalEntry> journalEntry, JournalEntry newUpdatedEntry) {
        JournalEntry oldEntry = journalEntry.get();
        oldEntry.setTitle(newUpdatedEntry.getTitle() != null && !newUpdatedEntry.getTitle().isBlank() ? newUpdatedEntry.getTitle() : oldEntry.getTitle());
        oldEntry.setContent(newUpdatedEntry.getContent() != null && !newUpdatedEntry.getContent().equals("") ? newUpdatedEntry.getContent() : oldEntry.getContent());
        return journalEntryRepository.save(oldEntry);
    }


}
