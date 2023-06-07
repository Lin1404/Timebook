
package com.timebook.timebook.service;

import com.timebook.timebook.controllers.EventController;
import com.timebook.timebook.models.UserData;
import com.timebook.timebook.models.users.User;
import com.timebook.timebook.models.users.UserRepository;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Log logger = LogFactory.getLog(EventController.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Async
    public void saveUser(UserData user) {
        try {
            User userFound = userRepository.findByEmail(user.getEmail());
            if (userFound == null) {
                User userToSave = new User();
                userToSave.setCognitoId(user.getUsername());
                userToSave.setEmail(user.getEmail());
                userRepository.save(userToSave);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
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

    public User findUserByEmail(String userEmail) {
        User targetUser = userRepository.findByEmail(userEmail);
        return targetUser;
    }
}