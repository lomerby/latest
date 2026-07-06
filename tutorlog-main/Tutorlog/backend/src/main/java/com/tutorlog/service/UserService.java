package com.tutorlog.service;

import com.tutorlog.model.User;
import com.tutorlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public User registerUser(String fullName, String email, String password) {
        if(userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setSubscriptionLevel("FREE");
        user.setCreatedAt(LocalDateTime.now());
        user.setRole("USER");
        
        return userRepository.save(user);
    }
    
    public User loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        
        if(user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        if(!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        return user.get();
    }
    
    public User getUserById(String id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public User upgradeSubscription(String userId, String subscriptionLevel) {
        User user = getUserById(userId);
        user.setSubscriptionLevel(subscriptionLevel);
        user.setSubscriptionStartDate(LocalDateTime.now());
        user.setSubscriptionEndDate(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public long getTotalUsers() {
        return userRepository.count();
    }
    
    public long getPremiumUsers() {
        return userRepository.findBySubscriptionLevel("PREMIUM").size();
    }
}