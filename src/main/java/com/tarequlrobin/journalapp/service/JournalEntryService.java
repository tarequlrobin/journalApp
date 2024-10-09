package com.tarequlrobin.journalapp.service;

import com.tarequlrobin.journalapp.entity.JournalEntry;
import com.tarequlrobin.journalapp.entity.User;
import com.tarequlrobin.journalapp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Service
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    //private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        User user = userService.findUserByUserName(userName);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveEntry(user);
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry getJournalEntryById(ObjectId id) {
        return journalEntryRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteJournalEntryById(ObjectId id, String userName) {
        try {
            User user = userService.findUserByUserName(userName);
            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed){
                userService.saveEntry(user);
                journalEntryRepository.deleteById(id);
            }
        }
        catch (Exception e){
            log.info(e.getMessage());
            throw new RuntimeException("An error occurred while deleting journal entry", e);
        }
    }

    public User findByUserName(String userName){
        return userService.findUserByUserName(userName);
    }
}
