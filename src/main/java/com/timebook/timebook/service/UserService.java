
package com.timebook.timebook.service;

import com.timebook.timebook.users.User;
import com.timebook.timebook.users.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Add subscribed
    public void createSubscription(String subscribeToEmail, String subscribeFromEmail) {
        User fromUser = userRepository.findByEmail(subscribeFromEmail);
        User toUser = userRepository.findByEmail(subscribeToEmail);

        if(!fromUser.getSubscriptions().contains(toUser)){
            fromUser.getSubscriptions().add(toUser);
            toUser.getSubscribers().add(fromUser);
    
            userRepository.save(fromUser);
            userRepository.save(toUser);
        }
    }

    public String printUser(String userEmail) {
        User targetUser = userRepository.findByEmail(userEmail);
        return targetUser.toString();
    }

}