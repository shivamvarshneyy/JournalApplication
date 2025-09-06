package com.example.practiceJournal.entity;

import com.example.practiceJournal.constants.Sentiments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "journalEntries")
public class JournalEntry {
    @NonNull
    private String id;
    @NonNull
    private String title;
    private String content;
    private Sentiments sentiment;
    private LocalDateTime dateTime;
}
