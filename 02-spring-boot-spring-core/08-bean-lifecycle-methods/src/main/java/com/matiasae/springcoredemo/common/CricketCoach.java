package com.matiasae.springcoredemo.common;
import org.springframework.stereotype.Component;

import com.matiasae.springcoredemo.interfaces.ICoach;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class CricketCoach implements ICoach {
    public CricketCoach() {
        System.out.println("In constructor: " + getClass().getSimpleName());
    }
    
    @Override
    public String getDailyWorkout() {
        return "Practice fast bowling for 15 minutes.";
    }

    @PostConstruct
    public void doMyStartupStuff() {
        System.out.println("In @PostConstruct method: " + getClass().getSimpleName());
    }

    @PreDestroy
    public void doMyCleanupStuff() {
        System.out.println("In @PreDestroy method: " + getClass().getSimpleName());
    } 

}
