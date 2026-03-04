package com.matiasae.springcoredemo.rest;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;

import com.matiasae.springcoredemo.interfaces.ICoach;

@RestController
public class DemoController {
    private ICoach coach;

    // @Autowired
    public DemoController(@Qualifier("baseballCoach") ICoach coach) {
        this.coach = coach;
    }
    
    @GetMapping("/dailyworkout")
    public String getDailyWorkout() {
        return coach.getDailyWorkout();
    }
}
