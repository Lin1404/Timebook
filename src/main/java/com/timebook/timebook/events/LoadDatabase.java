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
            log.info("Preloading " + repository.save(new event(1, "Calvin123@gmail.com", "Leetcode problems", "praticing question solving skill", "2023-04-29 21:05:01", "2023-04-29 23:05:16", 2)));
            log.info("Preloading " + repository.save(new event(2, "Ada123@gmail.com", "frontend project", "praticing frontend developing skill", "2023-04-22 21:05:02", "2023-04-22 23:05:26", 3)));
            log.info("Preloading " + repository.save(new event(3, "Ben123@gmail.com", "backend project", "praticing backend development skill", "2023-04-26 21:05:03", "2023-04-27 19:05:36", 4)));
            log.info("Preloading " + repository.save(new event(4, "Ada123@gmail.com", "frontend project", "praticing frontend developing skill", "2023-04-29 21:05:04", "2023-04-29 23:05:46", 3)));
            log.info("Preloading " + repository.save(new event(5, "Ada123@gmail.com", "frontend project", "praticing frontend developing skill", "2023-04-27 21:05:05", "2023-04-27 23:05:06", 3)));
            log.info("Preloading " + repository.save(new event(6, "Ada123@gmail.com", "frontend project", "praticing frontend developing skill", "2023-05-27 21:05:05", "2023-05-27 23:05:06", 3)));

        };
    }
}
