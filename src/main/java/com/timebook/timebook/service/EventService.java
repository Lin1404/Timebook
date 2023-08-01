package com.timebook.timebook.service;

import com.timebook.timebook.models.events.Event;
import com.timebook.timebook.models.events.EventRepository;
import com.timebook.timebook.models.users.User;

import jakarta.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
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

    public Event save(Event requestEvent) throws ValidationException {
        if (!this.isEventValid(requestEvent)) {
            throw new ValidationException("Event validation failed.");
        }
        return eventRepository.save(requestEvent);
    }

    private boolean isEventValid(Event event) {
        if (event.getStartDateTime() == null ||
                event.getEndDateTime() == null ||
                event.getStartDateTime().isAfter(event.getEndDateTime())) {
            return false;
        }
        return true;
    }

    public void delete(long id) {
        eventRepository.deleteById(id);
    }

    private String getStartDatetimeStr(String date) {
        LocalDate selectedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();
        // TODO: Move to client side so client and server can be in different timezones.
        ZoneOffset offset = ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now());
        // Get the start of the week in related to dynamic local timezone;
        LocalDateTime startDateTime = selectedDate.with(fieldUS, 1).atStartOfDay()
                .minusSeconds(offset.getTotalSeconds());

        return startDateTime.toString();
    }

    public List<Event> getEventsWithSubscription(String period, String date, String userEmail) {
        User user = this.userService.findUserByEmail(userEmail);

        this.userService.updateLastView(period, user);

        List<String> allEmails = new ArrayList<>();
        allEmails.add(userEmail);
        allEmails.addAll(user.getSubscriptions().stream().map(User::getEmail)
                .collect(Collectors.toList()));

        String startDateTimeStr = this.getStartDatetimeStr(date);
        List<Event> allEvents = allEmails.parallelStream().map(email -> {
            List<Event> events = this.eventRepository.findAllByEmailFromStartDateTimeForAnInterval(
                    email,
                    startDateTimeStr,
                    String.format("1 %s", period)); // 1 week || 1 month || 1 year
            return events;
        }).flatMap(List::stream).collect(Collectors.toList());

        return allEvents;
    }

    public boolean isMatchedOwner(long eventId, String ownerEmail) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (!event.isPresent()) {
            return false;
        }
        return event.get().getEmail().equals(ownerEmail);
    }
}
