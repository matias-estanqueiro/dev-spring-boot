package com.matiasae.springcoredemo;

import org.springframework.stereotype.Component;

@Component
public class CricketCoach implements ICoach {
    @Override
    public String getDailyWorkout() {
        return "Practice fast bowling for 15 minutes.";
    }

}
