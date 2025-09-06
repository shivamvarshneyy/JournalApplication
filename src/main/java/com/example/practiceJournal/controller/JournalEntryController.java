package com.example.practiceJournal.controller;

import com.example.practiceJournal.entity.JournalEntry;
import com.example.practiceJournal.entity.User;
import com.example.practiceJournal.service.JournalEntryService;
import com.example.practiceJournal.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
@AllArgsConstructor
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> addEntry(@RequestBody JournalEntry myEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        boolean b = user.getJournalEntries().stream().anyMatch(x -> x.getTitle().equalsIgnoreCase(myEntry.getTitle()));
        if(b){
            return new ResponseEntity<>("No Content Added, Entry already exist with same title in DB", HttpStatus.NOT_ACCEPTABLE);
        }
        journalEntryService.saveEntry(myEntry,username);
        return new ResponseEntity<>("Entry Added Successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getEntry(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        if(!user.getJournalEntries().isEmpty()){
            return new ResponseEntity<>(user.getJournalEntries(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("/id/{myId}")
    public ResponseEntity<String> deleteEntryById(@PathVariable String myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(journalEntryService.findJournalById(myId) != null){
            User user = userService.findByUsername(username);
            user.getJournalEntries().removeIf(x->x.getId().equals(myId));
            journalEntryService.deleteJournalById(myId);
            return new ResponseEntity<>("Entry Removed Successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("No Such Entry Present in Database",HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<?> getEntryById(@PathVariable String myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> list = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();
        if(!list.isEmpty()){
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>("No Such Entry Present in Database",HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<String> modifyJournalById(@PathVariable String myId,
                                                    @RequestBody JournalEntry myEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        boolean b = user.getJournalEntries().stream().anyMatch(x -> x.getTitle().equalsIgnoreCase(myEntry.getTitle()));
        if(b){
            return new ResponseEntity<>("No Content Updated, Entry already exist with same title in DB, choose different title", HttpStatus.NOT_ACCEPTABLE);
        }
        JournalEntry entry = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList().getFirst();
        if(entry != null){
            entry.setTitle(myEntry.getTitle());
            if(myEntry.getContent()!=null && !myEntry.getContent().isEmpty()){
                entry.setContent(myEntry.getContent());
            }
            journalEntryService.saveEntry(entry);
            return new ResponseEntity<>("Content Updated Sucessfully", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("No Such Entry Present in Database with id",HttpStatus.OK);
    }

}
