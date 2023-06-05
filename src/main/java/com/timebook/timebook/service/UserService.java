
package com.timebook.timebook.service;

import com.timebook.timebook.models.UserData;
import com.timebook.timebook.users.User;
import com.timebook.timebook.users.UserRepository;
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

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Add subscribed
    public User subscribe(String subscribedEmail, String targetUserEmail) {
        User targetUser = userRepository.findByEmail(targetUserEmail);
        User subscriUser = userRepository.findByEmail(subscribedEmail);

        return targetUser;
    }
}