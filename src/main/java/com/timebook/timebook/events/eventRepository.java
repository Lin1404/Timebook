package com.timebook.timebook.events;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface eventRepository extends JpaRepository<event, Long>{
    
    public List<event> findAllByEmail(String email);

}
