package com.springboot.app.rest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class FunRestController {

    @GetMapping("/")
    public String sayHello() {
        return "Hello, World!";
    }

}
