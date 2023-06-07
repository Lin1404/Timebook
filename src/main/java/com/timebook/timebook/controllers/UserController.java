package com.timebook.timebook.controllers;

import com.timebook.timebook.models.UserData;

import com.timebook.timebook.service.UserService;
import com.timebook.timebook.users.User;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Add subscribed
    @PostMapping(value = "v1/subscribe")
    public User addSubscription(@RequestBody String subscribeFromEmail, Authentication authentication) {
        UserData userInfo = (UserData) authentication.getPrincipal();
        return userService.createSubscription(subscribeFromEmail, userInfo.getEmail());

        // return userService.printUser(userInfo.getEmail());
    }
}
