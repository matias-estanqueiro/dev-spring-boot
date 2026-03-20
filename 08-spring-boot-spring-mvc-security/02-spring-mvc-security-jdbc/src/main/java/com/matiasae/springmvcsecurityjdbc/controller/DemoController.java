package com.matiasae.springmvcsecurityjdbc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
    @GetMapping("/")
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/leaders")
    public String showLeadersPage() {
        return "leaders";
    }

    @GetMapping("/admins")
    public String showAdminPage() {
        return "admins";
    }
}
