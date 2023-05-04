package com.timebook.timebook.events;

import org.springframework.data.jpa.repository.JpaRepository;

public interface eventRepository extends JpaRepository<event, Long>{
    
}
