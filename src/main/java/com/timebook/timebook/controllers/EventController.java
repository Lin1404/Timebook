package com.timebook.timebook.controllers;

import com.timebook.timebook.models.UserData;
import com.timebook.timebook.models.events.Event;
import com.timebook.timebook.service.EventService;
import com.timebook.timebook.service.UserService;

import net.minidev.json.JSONObject;

import org.springframework.web.bind.annotation.RequestMapping;
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
    public Event saveEvent(@RequestBody Event requestEvent, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        requestEvent.setEmail(user.getEmail());
        return eventService.post(requestEvent);
    }

    // Deleting an event
    @DeleteMapping("v1/events/{id}")
    public long deleteEvent(@PathVariable long id) {
        eventService.delete(id);
        return id;
    }

    // Get weekly events by user's email
    @RequestMapping(value = "/v1/calendar/week/{date}", method = RequestMethod.GET)
    public JSONObject weekly(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        userService.saveUser(user);
        return eventService.getEventsWithSubscription("week", date, user.getEmail());
    }

    // Get monthly events by user's email
    @RequestMapping(value = "/v1/calendar/month/{date}", method = RequestMethod.GET)
    public JSONObject monthly(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        userService.saveUser(user);
        return eventService.getEventsWithSubscription("month", date, user.getEmail());
    }

    // Get annually events by user's email
    @RequestMapping(value = "/v1/calendar/annual/{date}", method = RequestMethod.GET)
    public JSONObject annually(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        userService.saveUser(user);
        return eventService.getEventsWithSubscription("annual", date, user.getEmail());
    }
}