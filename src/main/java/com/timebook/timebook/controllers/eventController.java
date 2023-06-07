package com.timebook.timebook.controllers;

import com.timebook.timebook.users.User;
import com.timebook.timebook.users.UserRepository;

import com.timebook.timebook.events.Event;
import com.timebook.timebook.events.EventRepository;
import com.timebook.timebook.models.UserData;
import com.timebook.timebook.service.EventService;
import com.timebook.timebook.service.UserService;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
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
    public List<Event> weekly(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        userService.saveUser(user);
        return eventService.eventsFilter("week", date, user, eventRepository);
    }

    // Get monthly events by user's email
    @RequestMapping(value = "/v1/calendar/month/{date}", method = RequestMethod.GET)
    public List<Event> monthly(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        userService.saveUser(user);
        return eventService.eventsFilter("month", date, user, eventRepository);
    }

    // Get annually events by user's email
    @RequestMapping(value = "/v1/calendar/annual/{date}", method = RequestMethod.GET)
    public List<Event> annually(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        userService.saveUser(user);
        return eventService.eventsFilter("annual", date, user, eventRepository);
    }
}
