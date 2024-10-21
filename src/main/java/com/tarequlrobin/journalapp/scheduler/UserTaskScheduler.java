package com.tarequlrobin.journalapp.scheduler;

import com.tarequlrobin.journalapp.entity.JournalEntry;
import com.tarequlrobin.journalapp.entity.User;
import com.tarequlrobin.journalapp.repository.UserRepositoryImpl;
import com.tarequlrobin.journalapp.service.EmailService;
import com.tarequlrobin.journalapp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserTaskScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendSAMail(){
        List<User> userForSA = userRepository.getUserForSA();
        for(User user : userForSA){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<String> filteredList = journalEntries.stream()
                    .filter(x -> x.getDate()
                            .isAfter(LocalDateTime.now()
                                    .minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getContent())
                    .collect(Collectors.toList());

            String entries = String.join(" ", filteredList);
            String sentiment = sentimentAnalysisService.getSentiment(entries);

            emailService.sendEmail(user.getEmail(),
                    "Reminder Email",
                    sentiment);
        }
    }
}
