package com.example.practiceJournal.appCache;

import com.example.practiceJournal.entity.ConfigJournalAppEntity;
import com.example.practiceJournal.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppCache {
    private final ConfigJournalAppRepository configJournalAppRepository;

    public Map<String,String> APP_CACHE;

    @PostConstruct
    public void init(){
        APP_CACHE = new HashMap<>();
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity configJournalApp : all){
            APP_CACHE.put(configJournalApp.getKey(),configJournalApp.getValue());
        }
    }
}
