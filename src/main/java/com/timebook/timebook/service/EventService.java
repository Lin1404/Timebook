package com.timebook.timebook.service;

import com.timebook.timebook.models.events.Event;
import com.timebook.timebook.models.events.EventRepository;
import com.timebook.timebook.models.users.User;

import java.util.ArrayList;
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
    private final UserService userService;
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    public Event save(Event requestEvent) {
        return eventRepository.save(requestEvent);
    }

    public void delete(long id) {
        eventRepository.deleteById(id);
    }

    public Predicate<Event> createDateFilter(String period, String date) {
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

        Predicate<Event> datefilter = e -> !((Timestamp.valueOf(e.getEndDateTime()).toLocalDateTime()
                .isBefore(startDateTime)
                || Timestamp.valueOf(e.getStartDateTime()).toLocalDateTime().isAfter(endDateTime)));

        return datefilter;
    }

    public List<Event> getEventsWithSubscription(String period, String date, String userEmail) {
        User fromUser = userService.findUserByEmail(userEmail);
        List<String> subscritionList = fromUser.getSubscriptions().stream().map(User::getEmail)
                .collect(Collectors.toList());

        List<Event> targetEvents = new ArrayList<>();

        Predicate<Event> datefilter = this.createDateFilter(period, date);
        List<Event> userEvents = eventRepository.findAllByEmail(userEmail).stream().filter(datefilter)
                .collect(Collectors.toList());
        targetEvents.addAll(userEvents);

        subscritionList.forEach(sub -> {
            List<Event> subEvents = eventRepository.findAllByEmail(sub).stream().filter(datefilter)
                    .collect(Collectors.toList());
            targetEvents.addAll(subEvents);
        });

        return targetEvents;
    }
}
