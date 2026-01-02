package com.nebula.auth.service.impl;

import com.nebula.auth.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Hello Service Implementation
 */
@Slf4j
@Service
public class HelloServiceImpl implements HelloService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String getGreeting() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        log.debug("Generating default greeting at {}", timestamp);
        return String.format("Hello from Nebula Auth Service! Current time: %s", timestamp);
    }

    @Override
    public String getPersonalizedGreeting(String name) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        log.debug("Generating personalized greeting for {} at {}", name, timestamp);
        return String.format("Hello %s! Welcome to Nebula Auth Service. Current time: %s", name, timestamp);
    }
}
