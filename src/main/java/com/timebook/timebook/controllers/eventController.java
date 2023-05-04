package com.timebook.timebook.controllers;

import com.timebook.timebook.events.event;
import com.timebook.timebook.events.eventRepository;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


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
}
