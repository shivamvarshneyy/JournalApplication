package com.example.practiceJournal.repository;

import com.example.practiceJournal.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByusername(String username);
    void deleteByusername(String username);
}
