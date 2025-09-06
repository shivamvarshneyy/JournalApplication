package com.example.practiceJournal.controller;

import com.example.practiceJournal.appCache.AppCache;
import com.example.practiceJournal.entity.User;
import com.example.practiceJournal.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AppCache appCache;

    @PostMapping("/sign-up-as-admin")
    public ResponseEntity<String> createUser(@RequestBody User user){
        userService.saveNewAdmin(user);
        return new ResponseEntity<>("Admin account Created Successfully", HttpStatus.CREATED);
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUser(){
        List<User> users = userService.getUsers();
        if(!users.isEmpty()){
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>("No User present in DB",HttpStatus.NO_CONTENT);
    }

    @GetMapping("/clear-app-cache")
    public ResponseEntity<?> clearAppCache(){
        appCache.init();
        return new ResponseEntity<>("App CACHE cleared successfully",HttpStatus.OK);
    }
}
