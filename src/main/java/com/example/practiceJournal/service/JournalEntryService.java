package com.example.practiceJournal.service;

import com.example.practiceJournal.entity.JournalEntry;
import com.example.practiceJournal.entity.User;
import com.example.practiceJournal.repository.JournalEntryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;

    @Transactional
    public void saveEntry(JournalEntry myEntry, String username){
        try {
            User user = userService.findByUsername(username);
            myEntry.setDateTime(LocalDateTime.now());
            JournalEntry save = journalEntryRepository.save(myEntry);
            user.getJournalEntries().add(save);
            userService.saveUser(user);
        }catch (Exception e){
            log.error("An error occur while commiting the transaction : " + e);
        }
    }

    public void saveEntry(JournalEntry myEntry){
        myEntry.setDateTime(LocalDateTime.now());
        journalEntryRepository.save(myEntry);
    }
    
    public void deleteJournalById(String id){
        journalEntryRepository.deleteById(id);
    }

    public JournalEntry findJournalById(String id){
        return journalEntryRepository.findById(id).orElse(null);
    }

}
