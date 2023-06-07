
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
    public void createSubscription(String subscribeFromEmail, String subscribeToEmail) {
        User targetUser = userRepository.findByEmail(subscribeToEmail);
        User subscriUser = userRepository.findByEmail(subscribeFromEmail);
        List<User> subscribedList = targetUser.getSubscribed();
        subscribedList.add(subscriUser);
        targetUser.setSubscribed(subscribedList);

        List<User> subscriberList = subscriUser.getSubscriber();
        subscriberList.add(targetUser);
        subscriUser.setSubscriber(subscriberList);

        userRepository.save(targetUser);
        userRepository.save(subscriUser);
    }
}