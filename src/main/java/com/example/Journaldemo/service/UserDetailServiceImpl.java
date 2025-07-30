package com.example.Journaldemo.service;

import com.example.Journaldemo.entity.User;
import com.example.Journaldemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Trying to authenticate user: " + username);
        User user = userRepository.findByusername(username);
        if (user != null) {
            System.out.println("Found user: " + user.getUsername());
            System.out.println("Encoded password: " + user.getPassword());
            System.out.println("Roles: " + user.getRoles());

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}
