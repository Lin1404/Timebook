
package com.timebook.timebook.service;

import com.timebook.timebook.users.User;
import com.timebook.timebook.users.UserRepository;


import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Add subscribed
    public User createSubscription(String subscribeFromEmail, String subscribeToEmail) {
        User targetUser = userRepository.findByEmail(subscribeToEmail);
        User subscribeUser = userRepository.findByEmail(subscribeFromEmail);
        List<User> subscribedList = targetUser.getSubscribed();
        subscribedList.add(subscribeUser);
        targetUser.setSubscribed(subscribedList);

        List<User> subscriberList = subscribeUser.getSubscriber();
        subscriberList.add(targetUser);
        subscribeUser.setSubscriber(subscriberList);

        userRepository.save(targetUser);
        userRepository.save(subscribeUser);
        return subscribeUser;
    }

    public User printUser(String userEmail) {
        User targetUser = userRepository.findByEmail(userEmail);
        return targetUser;
    }

}