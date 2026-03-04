package com.matiasae.springcoredemo.rest;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.matiasae.util.ICoach;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class DemoController {
    private final ICoach coach;

    // @Autowired
    public DemoController(ICoach coach) {
        this.coach = coach;
    }

    @GetMapping("/dailyworkout")
    public String getDailyWorkout() {
        return coach.getDailyWorkout();
    }
}
