package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.repo.ApplicationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class TestController {

    ApplicationRepository applicationRepository;

    public TestController(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @RequestMapping("/test")
    public String showTestPage(Model model){
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attributes.getRequest().getSession();
        System.out.println(httpSession.getAttribute("isCandidate"));
        model.addAttribute("applications", applicationRepository.findAll());
        return "test";
    }
}
