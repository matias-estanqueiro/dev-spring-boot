package com.matiasae.springcoredemo.common;
import com.matiasae.springcoredemo.interfaces.ICoach;

import org.springframework.stereotype.Component;

@Component
public class TennisCoach implements ICoach {
    @Override
    public String getDailyWorkout() {
        return "Practice your backhand volley for 30 minutes.";
    }
}
