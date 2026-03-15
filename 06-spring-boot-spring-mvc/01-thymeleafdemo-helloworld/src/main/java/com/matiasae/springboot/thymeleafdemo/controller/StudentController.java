package com.matiasae.springboot.thymeleafdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.matiasae.springboot.thymeleafdemo.model.Student;

@Controller
public class StudentController {
    @Value("${countries}")
    private List<String> countryList;

    @Value("${programmingLanguages}")
    private List<String> programmingLanguages;

    @Value("${operatingSystems}")
    private List<String> operatingSystems;

    @GetMapping("/showStudentForm")
    public String showForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("countries", countryList);
        model.addAttribute("programmingLanguages", programmingLanguages);
        model.addAttribute("operatingSystems", operatingSystems);
        return "student-form";
    }

    @PostMapping("/processStudentForm")
    public String processForm(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("student", student);
        return "student-confirmation";
    }
}
