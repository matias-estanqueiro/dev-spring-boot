package com.matiasae.springcoredemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.matiasae.springcoredemo.interfaces.ICoach;
import com.matiasae.springcoredemo.common.SwimCoach;

@Configuration
public class SportConfig {
    // define bean for our swim coach and inject dependency
    @Bean("aquatic")
    public ICoach swimCoach() {
        return new SwimCoach();
    }
}
