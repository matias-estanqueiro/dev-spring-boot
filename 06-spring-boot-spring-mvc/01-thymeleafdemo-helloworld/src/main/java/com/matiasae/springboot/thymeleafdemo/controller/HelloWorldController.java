package com.matiasae.springboot.thymeleafdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloWorldController {
    @GetMapping("/showForm")
    public String showForm() {
        return "helloworld-form";
    }
    
    @PostMapping("/processFormVersionThree")
    public String processFormVersionThree(@RequestParam("studentName") String studentName, Model model) {
        studentName = studentName.toUpperCase();
        String result = "Hey My Friend (v3)! " + studentName;
        model.addAttribute("message", result);
        model.addAttribute("studentName", studentName);
        return "helloworld";
    }


    // @GetMapping("/processForm")
    // public String processForm() {
    //     return "helloworld";
    // }

    // @PostMapping("/processFormVersionTwo")
    // public String letsShoutDude(HttpServletRequest request, Model model) {
    //     String theName = request.getParameter("studentName");
    //     theName = theName.toUpperCase();
    //     String result = "Yo! (v2) " + theName;
    //     model.addAttribute("message", result);
    //     return "helloworld";
    // }
}
