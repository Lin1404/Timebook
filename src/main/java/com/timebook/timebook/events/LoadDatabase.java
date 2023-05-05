package com.timebook.timebook.events;

import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    
    @Bean
    CommandLineRunner initDatabase(eventRepository repository){
        return args ->{
            log.info("Preloading " + repository.save(new event(1, "Calvin123@gmail.com", "Leetcode problems", "praticing question solving skill", "Thu Apr 27 16:05:06 2023 EST", "Thu Apr 27 17:05:06 2023 EST", 2)));
            log.info("Preloading " + repository.save(new event(2, "Ada123@gmail.com", "frontend project", "praticing frontend developing skill", "Fri Apr 28 16:05:06 2023 EST", "Fri Apr 28 18:05:06 2023 EST", 3)));
            log.info("Preloading " + repository.save(new event(3, "Ben123@gmail.com", "backend project", "praticing backend development skill", "Sat Apr 29 16:05:06 2023 EST", "2023-04-27 19:05:06", 4)));
        };
    }
}
