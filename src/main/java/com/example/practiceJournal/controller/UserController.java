package com.example.practiceJournal.controller;

import com.example.practiceJournal.api.response.WeatherResponse;
import com.example.practiceJournal.entity.User;
import com.example.practiceJournal.repository.JournalEntryRepository;
import com.example.practiceJournal.repository.UserRepository;
import com.example.practiceJournal.service.UserService;
import com.example.practiceJournal.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final WeatherService weatherService;
    private static final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        if(user!=null){
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-profile")
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        if(user!=null){
            journalEntryRepository.deleteAll(user.getJournalEntries());
            user.getJournalEntries().clear();
            userRepository.delete(user);
            return new ResponseEntity<>("Your's credentials removed successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> modifyUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User oldUser = userService.findByUsername(username);
        if(oldUser != null){
            User existingUser = userService.findByUsername(user.getUsername());
            if(existingUser != null && !existingUser.getId().equals(oldUser.getId())){
                return new ResponseEntity<>("username already exist, try with another username", HttpStatus.NOT_ACCEPTABLE);
            }

            boolean isSamePassword = passwordEncoder.matches(user.getPassword(), oldUser.getPassword());
            boolean isSameUsername = oldUser.getUsername().equals(user.getUsername());
            boolean isSameEmail = oldUser.getEmail() != null && oldUser.getEmail().equals(user.getEmail());

            if(isSameUsername && isSamePassword && (user.getEmail() == null || isSameEmail)){
                return new ResponseEntity<>("Current credential can't be same as previous credential", HttpStatus.NOT_ACCEPTABLE);
            }

            oldUser.setUsername(user.getUsername());
            oldUser.setPassword(user.getPassword());
            if(user.getEmail() != null){
                oldUser.setEmail(user.getEmail());
            }

            userService.saveNewUser(oldUser);
            return new ResponseEntity<>("User credentials changed successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/city/{myCity}")
    public ResponseEntity<?> getWeather(@PathVariable String myCity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        WeatherResponse weather = weatherService.getWeather(myCity);
        if (weather != null){
            int temperature = weather.getCurrent().getTemperature();
            String region = weather.getLocation().getRegion();
            String country = weather.getLocation().getCountry();
            String description = weather.getCurrent().getWeatherDescriptions().getFirst();
            String response = String.format("Hi %s, current weather in %s, %s, %s is: %s°C with %s.", username, myCity, region, country, temperature, description);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        String res = String.format("Hi %s, facing issue in Fetching temperature of %s",username,myCity);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
