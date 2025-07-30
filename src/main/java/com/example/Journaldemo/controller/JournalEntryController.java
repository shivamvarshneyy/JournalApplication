package com.example.Journaldemo.controller;

import com.example.Journaldemo.entity.JournalEntry;
import com.example.Journaldemo.entity.User;
import com.example.Journaldemo.service.JournalEntryService;
import com.example.Journaldemo.service.UserService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByusername(username);
        List<JournalEntry> all = user.getJournalEntries();
        if(all.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(all,HttpStatus.FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> saveEntry(@RequestBody JournalEntry myEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            journalEntryService.saveEntry(myEntry,username);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<?> getById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByusername(username);
        List<JournalEntry> collect;
        collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();
        if(!collect.isEmpty()){
            JournalEntry id = journalEntryService.findById(myId);
            if(id!=null) {
                return new ResponseEntity<>(id,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean b = journalEntryService.deleteById(myId,username);
        if(b){
            return new ResponseEntity<>("true",HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> setValue(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry myEntry){
        JournalEntry old = journalEntryService.findById(myId);
        if(old==null){
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }else{
            if(myEntry.getTitle()!=null && !myEntry.getTitle().isEmpty())
                old.setTitle(myEntry.getTitle());
            if(myEntry.getContent()!=null && !myEntry.getContent().isEmpty())
                old.setContent(myEntry.getContent());
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        journalEntryService.saveEntry(old,username);
        return new ResponseEntity<>("Done",HttpStatus.ACCEPTED);
    }


}
