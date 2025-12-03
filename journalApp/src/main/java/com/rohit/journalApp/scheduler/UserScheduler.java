package com.rohit.journalApp.scheduler;

import com.rohit.journalApp.Entity.JournalEntry;
import com.rohit.journalApp.Entity.User;
import com.rohit.journalApp.config.AppCache;
import com.rohit.journalApp.repository.imp.UserMongoTemplateRepoImp;
import com.rohit.journalApp.service.imp.EmailServiceImp;
import com.rohit.journalApp.service.imp.SentimentServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private UserMongoTemplateRepoImp userMongoTemplateRepoImp;

    @Autowired
    private EmailServiceImp emailServiceImp;

    @Autowired
    private SentimentServiceImp sentimentServiceImp;

    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 10 * * *")
    public void fetchAndSendSA() {
        List<User> userWithSA = userMongoTemplateRepoImp.getUserWithSA();
        for (User user : userWithSA) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<String> content = journalEntries.stream()
                    .filter(x -> x.getDate()
                            .isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getContent())
                    .collect(Collectors.toList());
            String entries = String.join(" ", content);
            String sentiment = sentimentServiceImp.sentimentAnalysis(entries);
            emailServiceImp.sendMail((List<String>) user, sentiment, "Analysis");
        }
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void clearAppCache() {
        appCache.init();
    }
}
