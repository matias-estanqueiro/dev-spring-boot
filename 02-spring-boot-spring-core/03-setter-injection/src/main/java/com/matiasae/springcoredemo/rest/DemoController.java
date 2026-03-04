package com.matiasae.springcoredemo.rest;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.matiasae.springcoredemo.common.ICoach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class DemoController {
    private ICoach coach;

    // @Autowired
    // public void doSomething(ICoach coach) {
    //     this.coach = coach;
    // }

    @Autowired
    public void setCoach(ICoach coach) {
        this.coach = coach;
    }
    
    @GetMapping("/dailyworkout")
    public String getDailyWorkout() {
        return coach.getDailyWorkout();
    }
}
