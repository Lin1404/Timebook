package com.timebook.timebook.controllers;

import com.timebook.timebook.models.UserData;
import com.timebook.timebook.service.UserService;

import net.minidev.json.JSONObject;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    public ResponseEntity<String> addSubscription(@RequestBody JSONObject payload, Authentication authentication) {
        try {
            String subscribeToEmail = (String) payload.get("email");
            UserData userInfo = (UserData) authentication.getPrincipal();
            userService.createSubscription(subscribeToEmail, userInfo.getEmail());
            return ResponseEntity.status(HttpStatus.SC_ACCEPTED).body("Successfully Add Subscription.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Can not find user.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "v1/unsubscribe")
    public ResponseEntity<String> deleteSubscription(@RequestBody JSONObject payload,
            Authentication authentication) {
        try {
            String unSubscribeToEmail = (String) payload.get("email");
            UserData userInfo = (UserData) authentication.getPrincipal();
            userService.deleteSubscription(unSubscribeToEmail, userInfo.getEmail());
            return ResponseEntity.status(HttpStatus.SC_ACCEPTED).body("Successfully Delete Subscription.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Can not find user.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(null);
        }
    }

    // For testing
    @GetMapping(value = "v1/getUser")
    public String getUser(Authentication authentication) {
        UserData userInfo = (UserData) authentication.getPrincipal();
        return userService.getUser(userInfo.getEmail());
    }

    @PostMapping(value = "v1/searchEmails")
    public List<String> searchEmails(@RequestBody JSONObject payload, Authentication authentication) {
        String userInput = (String) payload.get("userInput");
        return userService.searchEmailsContainsString(userInput);

    }
}
