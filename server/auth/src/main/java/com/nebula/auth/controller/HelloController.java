package com.nebula.auth.controller;

import com.nebula.auth.service.HelloService;
import com.nebula.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello API Controller
 * Provides a simple greeting endpoint for testing
 */
@Slf4j
@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    /**
     * Simple hello endpoint
     * @return greeting message wrapped in Result
     */
    @GetMapping
    public Result<String> hello() {
        log.info("Hello endpoint called");
        String greeting = helloService.getGreeting();
        return Result.success(greeting, "Hello request successful");
    }

    /**
     * Personalized hello endpoint
     * @param name the name to greet
     * @return personalized greeting message wrapped in Result
     */
    @GetMapping("/personal")
    public Result<String> helloName(@RequestParam(defaultValue = "Guest") String name) {
        log.info("Hello endpoint called for name: {}", name);
        String greeting = helloService.getPersonalizedGreeting(name);
        return Result.success(greeting, "Personalized hello request successful");
    }
}
