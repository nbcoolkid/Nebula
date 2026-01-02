package com.nebula.auth.service;

/**
 * Hello Service Interface
 * Business logic for greeting functionality
 */
public interface HelloService {

    /**
     * Get default greeting message
     * @return greeting string
     */
    String getGreeting();

    /**
     * Get personalized greeting message
     * @param name the name to include in greeting
     * @return personalized greeting string
     */
    String getPersonalizedGreeting(String name);
}
