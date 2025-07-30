package com.example.Journaldemo.service;

import com.example.Journaldemo.controller.JournalEntryController;
import com.example.Journaldemo.entity.JournalEntry;
import com.example.Journaldemo.entity.User;
import com.example.Journaldemo.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

//    public static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);


    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    @Transactional
    public void saveEntry(JournalEntry myEntry, String username) {
        try {
            myEntry.setDate(LocalDateTime.now());
            User user = userService.findByusername(username);
            JournalEntry save = journalEntryRepository.save(null);
            boolean exists = user.getJournalEntries().stream()
                    .anyMatch(j -> j.getId().equals(save.getId()));
            if (!exists) {
                user.getJournalEntries().add(save);
            }
            userService.saveUser(user);
        } catch (Exception e) {
//            logger.trace("trace occured");
//            logger.debug("debug occured");
//            logger.info("info occured");
//            logger.warn("warn occured");
//            logger.error("error occured");
//            log.trace("trace occured");
//            log.debug("debug occured");
//            log.info("info occured");
//            log.warn("warn occured");
            log.error("error occured");
            throw new RuntimeException("An error occured while saving data " + e);
        }
    }

    public JournalEntry findById(ObjectId id) {
        return journalEntryRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username) {
        boolean remove = false;
        try {
            User user = userService.findByusername(username);
            remove = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(remove){
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        }catch (Exception e){
            throw new RuntimeException("An Error occured while deleting the entry",e);
        }
        return remove;
    }
}
