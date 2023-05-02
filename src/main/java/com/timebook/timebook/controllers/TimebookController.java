package com.timebook.timebook.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimebookController {

    @RequestMapping("/")
    public String homePage(){
        return "Welcome to Timebook!";
    }
    
}
