package com.timebook.timebook.controllers;

import com.timebook.timebook.models.UserData;

import com.timebook.timebook.events.event;
import com.timebook.timebook.events.eventRepository;

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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin
@RestController
public class eventController {

    private final eventRepository repository;

    public eventController(eventRepository repository) {
        this.repository = repository;
    }
    
    // Add / Update event
    @PostMapping(value="v1/events")
    public event saveEvent(@RequestBody event requestEvent, Authentication authentication) {
        UserData userInfo = (UserData)authentication.getPrincipal();
        requestEvent.setEmail(userInfo.getEmail());
        return repository.save(requestEvent);
    }

    // Deleting an event
    @DeleteMapping("v1/events/{id}")
    public long deleteEvent(@PathVariable long id) {
        repository.deleteById(id);
        return id;
    }

    static List<event> eventsFilter(String period, String date, String email, eventRepository repository) {
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

        return repository.findAllByEmail(email).stream().filter(datefilter).collect(Collectors.toList());
    }

    // Get weekly events by user's email
    @RequestMapping(value = "/v1/calendar/week/{date}", method = RequestMethod.GET)
    public List<event> weekly(@PathVariable("date") String date, Authentication authentication) {
        UserData userInfo = (UserData) authentication.getPrincipal();
        return eventsFilter("week", date, userInfo.getEmail(), repository);
    }

    // Get monthly events by user's email
    @RequestMapping(value = "/v1/calendar/month/{date}", method = RequestMethod.GET)
    public List<event> monthly(@PathVariable("date") String date, Authentication authentication) {
        UserData userInfo = (UserData) authentication.getPrincipal();
        return eventsFilter("month", date, userInfo.getEmail(), repository);
    }

    // Get annually events by user's email
    @RequestMapping(value = "/v1/calendar/annual/{date}", method = RequestMethod.GET)
    public List<event> annually(@PathVariable("date") String date, Authentication authentication) {
        UserData userInfo = (UserData) authentication.getPrincipal();
        return eventsFilter("annual", date, userInfo.getEmail(), repository);
    }
}
