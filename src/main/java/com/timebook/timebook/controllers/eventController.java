package com.timebook.timebook.controllers;

import com.timebook.timebook.events.event;
import com.timebook.timebook.events.eventRepository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class eventController {

    private final eventRepository repository;

    public eventController(eventRepository repository){
        this.repository = repository;
    }

    //List all the events
    @GetMapping(value = "v1/events")
    public List<event> all (){
        return repository.findAll();
    }
    
    //Adding an new event
    @PostMapping(value="v1/events")
    public event newEvent(@RequestBody event newEvent) {
        return repository.save(newEvent);
    }
    
    //Updating an event
    @PutMapping(value="v1/events/{id}")
    public event updatedEvent(@RequestBody event newEvent, @PathVariable long id){
        return repository.findById(id)
        .map(event -> {
            event.setEmail(newEvent.getEmail());
            event.setTitle(newEvent.getTitle());
            event.setDescription(newEvent.getDescription());
            event.setStartDateTime(newEvent.getStartDateTime());
            event.setEndDateTime(newEvent.getEndDateTime());
            event.setPriority(newEvent.getPriority());
            return repository.save(event);
        }
        ).orElseGet(()->{
            newEvent.setId(id);
            return repository.save(newEvent);
        });
    }

    //Deleting an event
    @DeleteMapping("v1/events/{id}")
    public void deleteEvent(@PathVariable long id){
        repository.deleteById(id);
    }

    static List<event> eventsFilter(String period, String date, String email, eventRepository repository){
        LocalDate selectedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        switch(period){
            case "weekly":
            startDate = selectedDate.with(DayOfWeek.MONDAY);
            endDate = startDate.plusDays(7);
            break;

            case "monthly":
            startDate = selectedDate.withDayOfMonth(1);
            endDate = selectedDate.plusMonths(1).withDayOfMonth(1);
            break;

            case "annually":
            startDate = selectedDate.withDayOfYear(1);
            endDate = selectedDate.plusYears(1).withDayOfYear(1);
            break;

            default:;
        }
        LocalDateTime startDateTime = startDate.atStartOfDay().atZone(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDateTime = endDate.atStartOfDay().atZone(ZoneOffset.UTC).toLocalDateTime();

        Predicate<event> datefilter = e -> 
            (Timestamp.valueOf(e.getEndDateTime()).toLocalDateTime().isBefore(startDateTime)
            || Timestamp.valueOf(e.getStartDateTime()).toLocalDateTime().isAfter(endDateTime))
            ? false : true;

        return repository.findAllByEmail(email).stream().filter(datefilter).collect(Collectors.toList());
    }

    //Get weekly events by user's email 
    @RequestMapping(value = "/v1/calendar/week/{date}/{email}", method = RequestMethod.GET)
    public List<event> weekly(@PathVariable ("date") String date, @PathVariable ("email") String email){
        return eventsFilter("weekly", date, email, repository);
    }

    //Get monthly events by user's email     
    @RequestMapping(value = "/v1/calendar/month/{date}/{email}", method = RequestMethod.GET)
    public List<event> monthly(@PathVariable ("date") String date, @PathVariable ("email") String email){
        return eventsFilter("monthly", date, email, repository);
    }

    //Get annually events by user's email 
    @RequestMapping(value = "/v1/calendar/annual/{date}/{email}", method = RequestMethod.GET)
    public List<event> annually(@PathVariable ("date") String date, @PathVariable ("email") String email){
        return eventsFilter("annually", date, email, repository);
    }
}
