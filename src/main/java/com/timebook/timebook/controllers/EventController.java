package com.timebook.timebook.controllers;

import com.timebook.timebook.models.UserData;
import com.timebook.timebook.models.events.Event;
import com.timebook.timebook.service.EventService;
import com.timebook.timebook.service.UserService;

import jakarta.validation.ValidationException;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@EnableAsync
@CrossOrigin
@RestController
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    // Add / Update event
    @PostMapping(value = "v1/events")
    public ResponseEntity<?> saveEvent(@RequestBody Event requestEvent, Authentication authentication) {
        try {
            UserData user = (UserData) authentication.getPrincipal();
            requestEvent.setEmail(user.getEmail());
            if (requestEvent.getIsVisible() == null) {
                requestEvent.setIsVisible(true);
            }
            return ResponseEntity.ok(eventService.save(requestEvent));

        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Event is invalid.");
        }
    }

    // Deleting an event
    @DeleteMapping("v1/events/{id}")
    public long deleteEvent(@PathVariable long id) {
        eventService.delete(id);
        return id;
    }

    // Get weekly events by user's email
    @RequestMapping(value = "/v1/calendar/week/{date}", method = RequestMethod.GET)
    public List<Event> weekly(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        userService.saveUser(user);
        return eventService.getEventsWithSubscription("week", date, user.getEmail());
    }

    // Get monthly events by user's email
    @RequestMapping(value = "/v1/calendar/month/{date}", method = RequestMethod.GET)
    public List<Event> monthly(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        userService.saveUser(user);
        return eventService.getEventsWithSubscription("month", date, user.getEmail());
    }

    // Get annually events by user's email
    @RequestMapping(value = "/v1/calendar/annual/{date}", method = RequestMethod.GET)
    public List<Event> annually(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        userService.saveUser(user);
        return eventService.getEventsWithSubscription("year", date, user.getEmail());
    }
}
