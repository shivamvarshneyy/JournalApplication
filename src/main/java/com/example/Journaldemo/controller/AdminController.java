package com.example.Journaldemo.controller;

import com.example.Journaldemo.entity.User;
import com.example.Journaldemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    public final UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> gelAllUsers(){
        List<User> all = userService.getAll();
        if(all.isEmpty() && all!=null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(all,HttpStatus.FOUND);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody User user){
        userService.saveAdmin(user);
        ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.CREATED);
        return objectResponseEntity;
    }
}
