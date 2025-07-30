package com.example.Journaldemo.controller;

import com.example.Journaldemo.entity.User;
import com.example.Journaldemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/check")
    public String isOk() {
        return "Everything is Fine";
    }

    @PostMapping
    public ResponseEntity<?> saveEntry(@RequestBody User user){
        userService.saveNewUser(user); // hashes password
        return new ResponseEntity<>("Done", HttpStatus.ACCEPTED);
    }

}
