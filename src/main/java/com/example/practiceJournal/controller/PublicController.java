package com.example.practiceJournal.controller;

import com.example.practiceJournal.entity.User;
import com.example.practiceJournal.service.EmailService;
import com.example.practiceJournal.service.UserDetailsServiceImpl;
import com.example.practiceJournal.service.UserService;
import com.example.practiceJournal.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

//@Profile("cloud")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/public")
public class PublicController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Profile("cloud")
    @GetMapping("/check")
    public ResponseEntity<String> healthCheck() {
        log.info("All Good");
        return new ResponseEntity<>("All Good", HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> createUser(@RequestBody User user){
        User existingUser = userService.findByUsername(user.getUsername());
        if(existingUser != null){
            return new ResponseEntity<>("username already exist, try with another username", HttpStatus.NOT_ACCEPTABLE);
        }
        userService.saveNewUser(user);
        return new ResponseEntity<>("Account Created Successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logInUser(@RequestBody User user){
       try {
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
           UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
           String token = jwtUtil.generateJwtToken(user.getUsername());
           return new ResponseEntity<>(token,HttpStatus.ACCEPTED);
       } catch (RuntimeException e) {
           log.error("Invalid User's credential");
           return new ResponseEntity<>("Invalid User's credential",HttpStatus.NOT_FOUND);
       }
    }

}
