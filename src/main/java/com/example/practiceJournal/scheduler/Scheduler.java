package com.example.practiceJournal.scheduler;

import com.example.practiceJournal.constants.Sentiments;
import com.example.practiceJournal.entity.JournalEntry;
import com.example.practiceJournal.entity.User;
import com.example.practiceJournal.repository.UserRepositoryImpl;
import com.example.practiceJournal.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final UserRepositoryImpl userRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * SUN")
    //@Scheduled(cron = "*/5 * * * * *")
    public void fetchUserAndSendMailForSentimentAnalysis(){
        List<User> users = userRepository.getUsersForSentimentAnalysis();
        for(User user: users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiments> sentiments = journalEntries.stream().filter(x -> x.getDateTime().isAfter(LocalDateTime.now().minusDays(7))).map(x -> x.getSentiment()).collect(Collectors.toList());
            Map<Sentiments,Integer> sentimentsCount = new HashMap<>();
            for (Sentiments senti:sentiments){
                if(senti != null) {
                    sentimentsCount.put(senti,sentimentsCount.getOrDefault(senti,0)+1);
                }
            }
            Integer maxCount = 0;
            Sentiments maximumTimesOccuredSentiment = null;
            for(Map.Entry<Sentiments,Integer> map : sentimentsCount.entrySet()){
                if(map.getValue()>maxCount){
                    maxCount = map.getValue();
                    maximumTimesOccuredSentiment = map.getKey();
                }
            }

            if(maximumTimesOccuredSentiment != null){
                emailService.sendMail(user.getEmail(),"Happy Sunday","Sentiment Analysis for last week is : "+maximumTimesOccuredSentiment);
            }
        }
    }
}
