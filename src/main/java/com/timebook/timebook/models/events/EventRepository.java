package com.timebook.timebook.models.events;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    public List<Event> findAllByEmail(String email);

    @Query(value = "SELECT * FROM event " +
            "WHERE email = :email " +
            "AND start_date_time " +
            "BETWEEN :start\\:\\:TIMESTAMP AND :end\\:\\:TIMESTAMP", nativeQuery = true)
    public List<Event> findAllByEmailAndBetween(
            @Param("email") String email,
            @Param("start") String start,
            @Param("end") String end);

    @Query(value = "SELECT * FROM event " +
            "WHERE email = :email " +
            "AND start_date_time " +
            "BETWEEN :startDateTime\\:\\:TIMESTAMP " +
            "AND (:startDateTime\\:\\:TIMESTAMP + :interval\\:\\:INTERVAL)", nativeQuery = true)
    public List<Event> findAllByEmailFromStartDateTimeForAnInterval(
            @Param("email") String email,
            @Param("startDateTime") String startDateTime,
            @Param("interval") String interval);

}
