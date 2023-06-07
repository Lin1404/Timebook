package com.timebook.timebook.models.events;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    public List<Event> findAllByEmail(String email);

}
