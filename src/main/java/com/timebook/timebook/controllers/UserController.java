package com.timebook.timebook.controllers;

import com.timebook.timebook.models.UserData;
import com.timebook.timebook.models.users.User;
import com.timebook.timebook.service.UserService;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<String> addSubscription(@RequestBody String subscribeToEmail, Authentication authentication) {
        try {
            UserData userInfo = (UserData) authentication.getPrincipal();
            userService.createSubscription(subscribeToEmail, userInfo.getEmail());
            return ResponseEntity.status(HttpStatus.SC_ACCEPTED).body("Successfully Add Subscription.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Expectation Failed from Client (CODE 400)\n");
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "v1/unsubscribe")
    public ResponseEntity<String> deleteSubscription(@RequestBody String unSubscribeToEmail,
            Authentication authentication) {
        try {
            UserData userInfo = (UserData) authentication.getPrincipal();
            userService.deleteSubscription(unSubscribeToEmail, userInfo.getEmail());
            return ResponseEntity.status(HttpStatus.SC_ACCEPTED).body("Successfully Delete Subscription.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Expectation Failed from Client (CODE 400)\n");
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "v1/lastview")
    public User updateLastView(@RequestBody String view, Authentication authentication) {
        UserData userInfo = (UserData) authentication.getPrincipal();
        return userService.updateLastView(view, userInfo.getEmail());
    }

    // For testing
    @GetMapping(value = "v1/getUser")
    public User getUser(Authentication authentication) {
        UserData userInfo = (UserData) authentication.getPrincipal();
        return userService.getUser(userInfo.getEmail());
    }
}
