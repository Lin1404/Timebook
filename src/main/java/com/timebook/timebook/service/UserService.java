
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
        User toUser = userRepository.findByEmail(subscribeToEmail);
        User fromUser = userRepository.findByEmail(subscribeFromEmail);
        fromUser.getSubscriptions().add(toUser);
        toUser.getSubscribers().add(fromUser);

        userRepository.save(fromUser);
        userRepository.save(toUser);
    }

    public User printUser(String userEmail) {
        User targetUser = userRepository.findByEmail(userEmail);
        return targetUser;
    }

}