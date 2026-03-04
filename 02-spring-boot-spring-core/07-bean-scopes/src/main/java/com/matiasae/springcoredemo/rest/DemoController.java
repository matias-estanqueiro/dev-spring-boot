package com.matiasae.springcoredemo.rest;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;

import com.matiasae.springcoredemo.interfaces.ICoach;


@RestController
public class DemoController {
    private ICoach coach;
    private ICoach anotherCoach;

    // @Autowired
    public DemoController(@Qualifier("cricketCoach") ICoach coach, @Qualifier("cricketCoach") ICoach anotherCoach) {
        System.out.println("In constructor: " + getClass().getSimpleName());
        this.coach = coach;
        this.anotherCoach = anotherCoach;
    }
    
    @GetMapping("/dailyworkout")
    public String getDailyWorkout() {
        return coach.getDailyWorkout();
    }

    @GetMapping("/check")
    public String check() {
        return "Pointing to the same object: " + (coach == anotherCoach);
    }
}
