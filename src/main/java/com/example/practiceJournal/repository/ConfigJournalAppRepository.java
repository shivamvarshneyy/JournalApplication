package com.example.practiceJournal.repository;

import com.example.practiceJournal.entity.ConfigJournalAppEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity,String> {
}
