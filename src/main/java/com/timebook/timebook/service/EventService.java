package com.timebook.timebook.service;

import com.timebook.timebook.events.Event;
import com.timebook.timebook.events.EventRepository;
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

import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event post(Event requestEvent) {
        return eventRepository.save(requestEvent);
    }

    public void delete(long id) {
        eventRepository.deleteById(id);
    }

    List<Event> eventsFilter(String period, String date, UserData user) {
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

        Predicate<Event> datefilter = e -> (Timestamp.valueOf(e.getEndDateTime()).toLocalDateTime()
                .isBefore(startDateTime)
                || Timestamp.valueOf(e.getStartDateTime()).toLocalDateTime().isAfter(endDateTime))
                        ? false
                        : true;

        return eventRepository.findAllByEmail(user.getEmail()).stream().filter(datefilter).collect(Collectors.toList());
    }
}
