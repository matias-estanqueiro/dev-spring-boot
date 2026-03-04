package com.matiasae.springcoredemo.common;
import com.matiasae.springcoredemo.interfaces.ICoach;

import org.springframework.stereotype.Component;

@Component
public class BaseballCoach implements ICoach {
    public BaseballCoach() {
        System.out.println("In constructor: " + getClass().getSimpleName());
    }
    
    @Override
    public String getDailyWorkout() {
        return "Practice your fastball for 30 minutes.";
    }
}
