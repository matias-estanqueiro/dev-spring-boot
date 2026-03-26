package com.matiasae.aopdemo.service;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class TrafficFortuneServiceImpl implements TrafficFortuneService {
    @Override
    public String getFortune() {
        // simulate a delay
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Done! With delay!";
    }

    @Override
    public String getFortune(boolean tripWire) {
        if (tripWire) {
            throw new RuntimeException("Exception thrown from getFortune() method!");
        }
        return "All is ok!";
    }

}
