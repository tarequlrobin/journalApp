package com.tarequlrobin.journalapp.controller;

import com.tarequlrobin.journalapp.entity.JournalEntry;
import com.tarequlrobin.journalapp.entity.User;
import com.tarequlrobin.journalapp.service.JournalEntryService;
import com.tarequlrobin.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){
        User user = userService.findUserByUserName(userName);
        List<JournalEntry> journalEntryList = user.getJournalEntries();
        if(!journalEntryList.isEmpty() && journalEntryList != null){
            return new ResponseEntity<>(journalEntryList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName}")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry, @PathVariable String userName){
        try {
            journalEntryService.saveEntry(journalEntry, userName);
            Optional<JournalEntry> optionalJournalEntry = Optional.of(journalEntry);
            return new ResponseEntity<>(optionalJournalEntry.get(), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id){
        //return journalEntryService.getJournalEntryById(id);
        Optional<JournalEntry> optionalJournalEntry = Optional.ofNullable(journalEntryService.getJournalEntryById(id));
        /*
        if (optionalJournalEntry.isPresent()){
            return new ResponseEntity<>(optionalJournalEntry.get(), HttpStatus.OK) ;
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND) ;
        */
        return optionalJournalEntry.map(journalEntry -> new ResponseEntity<>(journalEntry, HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("id/{userName}/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id, @PathVariable String userName){
        Optional<ObjectId> optionalObjectId = Optional.ofNullable(id);
        if(optionalObjectId.isPresent()){
            journalEntryService.deleteJournalEntryById(id, userName);
            return new ResponseEntity<JournalEntry>(HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{userName}/{id}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId id,
                                        @RequestBody JournalEntry newEntry,
                                        @PathVariable String userName){
        JournalEntry oldEntry = journalEntryService.getJournalEntryById(id);
        if(oldEntry != null) {
            oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ?
                    newEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ?
                    newEntry.getContent() : oldEntry.getContent());

            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
