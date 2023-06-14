
package com.timebook.timebook.service;

import com.timebook.timebook.controllers.EventController;
import com.timebook.timebook.models.UserData;
import com.timebook.timebook.models.users.User;
import com.timebook.timebook.models.users.UserRepository;

import net.minidev.json.JSONObject;

import org.springframework.stereotype.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Log logger = LogFactory.getLog(EventController.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

        if (fromUser != null && !fromUser.getSubscriptions().contains(toUser)) {
            fromUser.getSubscriptions().add(toUser);
            toUser.getSubscribers().add(fromUser);
    
            userRepository.save(fromUser);
            userRepository.save(toUser);
        }
    }

    public void deleteSubscription(String unSubscribeToEmail, String unsubscribeFromEmail) {
        User fromUser = userRepository.findByEmail(unsubscribeFromEmail);
        User toUser = userRepository.findByEmail(unSubscribeToEmail);

        if (fromUser != null && fromUser.getSubscriptions().contains(toUser)) {
            fromUser.getSubscriptions().remove(toUser);
            toUser.getSubscribers().remove(fromUser);

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

    public User updateLastView(String view, String userEmail) {
        User user = userRepository.findByEmail(userEmail);

        if (user.getMetadata() != null) {
            JSONObject metaData = user.getMetadata();
            metaData.put("lastView", view);
            userRepository.save(user);
        } else {
            JSONObject metaData = new JSONObject();
            metaData.put("lastView", view);
            user.setMetadata(metaData);
            userRepository.save(user);
        }
        return user;
    }

    public User getUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        return user;
    }
}