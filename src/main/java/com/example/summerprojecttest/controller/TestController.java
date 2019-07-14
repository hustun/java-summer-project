package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.repo.ApplicationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    ApplicationRepository applicationRepository;

    public TestController(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @RequestMapping("/test")
    public String showTestPage(Model model){
        model.addAttribute("applications", applicationRepository.findAll());
        return "test";
    }
}
