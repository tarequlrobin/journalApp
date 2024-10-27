package com.tarequlrobin.journalapp.scheduler;

import com.tarequlrobin.journalapp.entity.JournalEntry;
import com.tarequlrobin.journalapp.entity.User;
import com.tarequlrobin.journalapp.enums.Sentiment;
import com.tarequlrobin.journalapp.repository.UserRepositoryImpl;
import com.tarequlrobin.journalapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserTaskScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EmailService emailService;

    //@Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendSAMail(){
        List<User> userForSA = userRepository.getUserForSA();
        for(User user : userForSA){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate()
                            .isAfter(LocalDateTime.now()
                                    .minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment())
                    .collect(Collectors.toList());

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();

            for (Sentiment sentiment : sentiments) {
                if (sentiment != null){
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    mostFrequentSentiment = entry.getKey();
                    maxCount = entry.getValue();
                }
            }
            if (mostFrequentSentiment != null){
                emailService.sendEmail(user.getEmail(),
                        "Test Purpose Email",
                        mostFrequentSentiment.toString());
            }
        }
    }
}
