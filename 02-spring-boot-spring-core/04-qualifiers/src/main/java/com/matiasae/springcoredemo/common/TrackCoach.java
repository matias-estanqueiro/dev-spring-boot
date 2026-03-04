package com.matiasae.springcoredemo.common;
import com.matiasae.springcoredemo.interfaces.ICoach;

import org.springframework.stereotype.Component;

@Component
public class TrackCoach implements ICoach {
    @Override
    public String getDailyWorkout() {
        return "Run a 5k every morning.";
    }
}
