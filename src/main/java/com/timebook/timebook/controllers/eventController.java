package com.timebook.timebook.controllers;

import com.timebook.timebook.users.User;
import com.timebook.timebook.users.UserRepository;

import com.timebook.timebook.events.event;
import com.timebook.timebook.events.eventRepository;
import com.timebook.timebook.models.UserData;

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
public class eventController {
    private static final Log logger = LogFactory.getLog(eventController.class);
    private final eventRepository eventRepository;

    private final UserRepository userRepository;

    public eventController(eventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }
    
    // Add / Update event
    @PostMapping(value="v1/events")
    public event saveEvent(@RequestBody event requestEvent, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        requestEvent.setEmail(user.getEmail());
        return eventRepository.save(requestEvent);
    }

    // Deleting an event
    @DeleteMapping("v1/events/{id}")
    public long deleteEvent(@PathVariable long id) {
        eventRepository.deleteById(id);
        return id;
    }

    @Async
    public void saveUser(UserData user) {
        try {
            User userFound = userRepository.findByEmail(user.getEmail());
            if (userFound == null) {
                User userToSave = new User();
                userToSave.setCognitoId(user.getUsername());
                userToSave.setEmail(user.getEmail());
                userRepository.save(userToSave);
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    static List<event> eventsFilter(String period, String date, UserData user, eventRepository eventRepository) {
        LocalDate selectedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();

        switch (period) {
            case "week":
                startDate = selectedDate.with(fieldUS, 1);
                endDate = startDate.plusDays(7);
                break;

            case "month":
                startDate = selectedDate.withDayOfMonth(1);
                endDate = selectedDate.plusMonths(1).withDayOfMonth(1);
                break;

            case "annual":
                startDate = selectedDate.withDayOfYear(1);
                endDate = selectedDate.plusYears(1).withDayOfYear(1);
                break;

            default:
                break;
        }
        LocalDateTime startDateTime = startDate.atStartOfDay().atZone(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDateTime = endDate.atStartOfDay().atZone(ZoneOffset.UTC).toLocalDateTime();

        Predicate<event> datefilter = e -> (Timestamp.valueOf(e.getEndDateTime()).toLocalDateTime()
                .isBefore(startDateTime)
                || Timestamp.valueOf(e.getStartDateTime()).toLocalDateTime().isAfter(endDateTime))
                        ? false
                        : true;

        return eventRepository.findAllByEmail(user.getEmail()).stream().filter(datefilter).collect(Collectors.toList());
    }

    // Get weekly events by user's email
    @RequestMapping(value = "/v1/calendar/week/{date}", method = RequestMethod.GET)
    public List<event> weekly(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        saveUser(user);
        return eventsFilter("week", date, user, eventRepository);
    }

    // Get monthly events by user's email
    @RequestMapping(value = "/v1/calendar/month/{date}", method = RequestMethod.GET)
    public List<event> monthly(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        return eventsFilter("month", date, user, eventRepository);
    }

    // Get annually events by user's email
    @RequestMapping(value = "/v1/calendar/annual/{date}", method = RequestMethod.GET)
    public List<event> annually(@PathVariable("date") String date, Authentication authentication) {
        UserData user = (UserData) authentication.getPrincipal();
        return eventsFilter("annual", date, user, eventRepository);
    }
}
