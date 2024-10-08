package com.tarequlrobin.journalapp.controller;

import com.tarequlrobin.journalapp.entity.JournalEntry;
import com.tarequlrobin.journalapp.entity.User;
import com.tarequlrobin.journalapp.service.JournalEntryService;
import com.tarequlrobin.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findUserByUserName(userName);
        List<JournalEntry> journalEntryList = user.getJournalEntries();
        if(!journalEntryList.isEmpty() && journalEntryList != null){
            return new ResponseEntity<>(journalEntryList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        journalEntryService.saveEntry(journalEntry, userName);
        Optional<JournalEntry> optionalJournalEntry = Optional.of(journalEntry);
        return new ResponseEntity<>(optionalJournalEntry.get(), HttpStatus.CREATED);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userByUserName = userService.findUserByUserName(userName);
        List<JournalEntry> journalEntryList = userByUserName.getJournalEntries()
                .stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if (!journalEntryList.isEmpty()){
            Optional<JournalEntry> optionalJournalEntry = Optional.ofNullable(journalEntryService.getJournalEntryById(id));
            return new ResponseEntity<>(optionalJournalEntry.get(), HttpStatus.OK) ;
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND) ;
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<ObjectId> optionalObjectId = Optional.ofNullable(id);
        if(optionalObjectId.isPresent()){
            journalEntryService.deleteJournalEntryById(id, userName);
            return new ResponseEntity<JournalEntry>(HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId id,
                                        @RequestBody JournalEntry newEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userByUserName = userService.findUserByUserName(userName);
        List<JournalEntry> journalEntryList = userByUserName.getJournalEntries()
                .stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        JournalEntry oldEntry = journalEntryService.getJournalEntryById(id);
        if (!journalEntryList.isEmpty()){
            Optional<JournalEntry> optionalJournalEntry = Optional.ofNullable(journalEntryService.getJournalEntryById(id));
            if(optionalJournalEntry.isPresent()){
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ?
                        newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ?
                        newEntry.getContent() : oldEntry.getContent());

                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
