package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.model.Candidate;
import com.example.summerprojecttest.services.CandidateService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    private CandidateService candidateService;

    public HomeController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping(value = {"/","/home"})
    public String index(Model model) {

        //get session
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attributes.getRequest().getSession();

        //if candidate logged in
        if ( httpSession.getAttribute("isCandidate") != null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Candidate candidate = candidateService.findByEmail(email);

            model.addAttribute("candidateObject", candidate);
        }

        return "index";

    }
}
