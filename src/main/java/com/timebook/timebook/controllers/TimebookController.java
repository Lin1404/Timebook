package com.timebook.timebook.controllers;

import com.timebook.timebook.events.event;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@RestController
public class TimebookController {

    @RequestMapping("/")
    public String homePage(){
        return "Welcome to Timebook!";
    }
    
    @RequestMapping(value = "/v1/calendar/week/{date}/{email}", method = RequestMethod.GET)
    public Integer weekly(@PathVariable ("date") Integer date, @PathVariable ("email") String email){
        return date;
    }

    @GetMapping("/v1/calendar/month/{date}/{email}")
    List<String> monthly() {
        return List.of("Hello", "monthly");
    }

    @GetMapping("/v1/calendar/annual/{date}/{email}")
    List<String> annually() {
        return List.of("Hello", "annually");
    }
}
