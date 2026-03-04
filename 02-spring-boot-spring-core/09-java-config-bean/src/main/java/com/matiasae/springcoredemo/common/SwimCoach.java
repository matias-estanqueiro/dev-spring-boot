package com.matiasae.springcoredemo.common;

import com.matiasae.springcoredemo.interfaces.ICoach;

// No @Component annotation here, so this class won't be registered as a Spring bean
public class SwimCoach implements ICoach {
    public SwimCoach() {
        System.out.println("In constructor: " + getClass().getSimpleName());
    }

    @Override
    public String getDailyWorkout() {
        return "Swim 1000 meters as a warm up.";
    }

}
