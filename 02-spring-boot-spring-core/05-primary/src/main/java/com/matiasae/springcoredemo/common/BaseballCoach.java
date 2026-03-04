package com.matiasae.springcoredemo.common;
import com.matiasae.springcoredemo.interfaces.ICoach;

import org.springframework.stereotype.Component;

@Component
public class BaseballCoach implements ICoach {
    @Override
    public String getDailyWorkout() {
        return "Practice your fastball for 30 minutes.";
    }
}
