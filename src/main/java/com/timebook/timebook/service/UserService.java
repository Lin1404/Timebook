
package com.timebook.timebook.service;

import com.timebook.timebook.models.UserData;
import com.timebook.timebook.models.users.User;
import com.timebook.timebook.models.users.UserRepository;

import net.minidev.json.JSONObject;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Log logger = LogFactory.getLog(UserService.class);

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
            this.logger.error(e.getMessage());
        }
    }

    // Add subscribed
    public void createSubscription(String subscribeToEmail, String subscribeFromEmail)
            throws UsernameNotFoundException, NullPointerException {
        User fromUser = userRepository.findByEmail(subscribeFromEmail);
        User toUser = userRepository.findByEmail(subscribeToEmail);

        if (toUser == null || fromUser == null) {
            throw new UsernameNotFoundException("Email not found.");
        }

        if (fromUser.getSubscriptions().contains(toUser)) {
            throw new NullPointerException("User has subscribed this email already.");
        }

        fromUser.getSubscriptions().add(toUser);
        toUser.getSubscribers().add(fromUser);

        userRepository.save(fromUser);
        userRepository.save(toUser);

    }

    public void deleteSubscription(String unSubscribeToEmail, String unsubscribeFromEmail)
            throws UsernameNotFoundException, NullPointerException {
        User fromUser = userRepository.findByEmail(unsubscribeFromEmail);
        User toUser = userRepository.findByEmail(unSubscribeToEmail);

        if (toUser == null || fromUser == null) {
            throw new UsernameNotFoundException("Email not found.");
        }

        if (!fromUser.getSubscriptions().contains(toUser)) {
            throw new NullPointerException("User has not subscribed this email.");
        }

        fromUser.getSubscriptions().remove(toUser);
        toUser.getSubscribers().remove(fromUser);

        userRepository.save(fromUser);
        userRepository.save(toUser);
    }

    public String printUser(String userEmail) {
        User targetUser = userRepository.findByEmail(userEmail);
        return targetUser.toString();
    }

    public User findUserByEmail(String userEmail) {
        User targetUser = userRepository.findByEmail(userEmail);
        return targetUser;
    }

    public void updateLastView(String view, User user) throws UsernameNotFoundException {
        JSONObject metaData = new JSONObject();
        if (user.getMetadata() != null) {
            metaData = user.getMetadata();
        }
        metaData.put("lastView", view);
        user.setMetadata(metaData);
        userRepository.save(user);
    }

    public String getUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        return user.toString();
    }
}