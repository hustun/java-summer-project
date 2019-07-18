package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.model.Candidate;
import com.example.summerprojecttest.model.Job;
import com.example.summerprojecttest.model.Skill;
import com.example.summerprojecttest.model.SkillsList;
import com.example.summerprojecttest.services.CandidateService;
import com.example.summerprojecttest.services.SkillService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class CandidateController {

    private CandidateService candidateService;
    private SkillService skillService;
    private ArrayList<Skill> skills = new ArrayList<>();

    public CandidateController(CandidateService candidateService, SkillService skillService) {
        this.candidateService = candidateService;
        this.skillService = skillService;
        this.skills = SkillsList.getSkillsList();
    }

    @RequestMapping("/candidates")
    public String showCandidatesPage(Model model){
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates";
    }

    @GetMapping("/candidate/newCandidateForm")
    public String newCandidate(Model model){
        RedirectView redirectView = new RedirectView();
        Candidate candidate = new Candidate();

        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println("Controller email: " + email);
        candidate.setEmail(email);

        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attributes.getRequest().getSession();
        candidate.setFirstName(httpSession.getAttribute("firstName").toString());
        candidate.setLastName(httpSession.getAttribute("lastName").toString());


        model.addAttribute("candidate", candidate);
        model.addAttribute("skills", skills);

        return "candidate/new";
    }

    @PostMapping("/candidate/new")
    public String addCandidate(@ModelAttribute("candidate") Candidate candidate, @RequestParam(value = "candidateSkill", required = false) List<String> skills){

        if(skills != null){
            System.out.println("List length :" +  skills.size());
            for (String skillName : skills){
                candidate.getSkills().add(skillService.findBySkillName(skillName));
                System.out.println(skillName);
            }
        }

        candidateService.save(candidate);


        return "redirect:/jobs";
    }

    @RequestMapping("/candidate/show/{id}")
    public String showCandidateById(@PathVariable String id, Model model){
        Candidate candidate = candidateService.findById(Integer.valueOf(id));
        model.addAttribute("candidate", candidate);
        model.addAttribute("skills", candidate.getSkills());
        model.addAttribute("applications", candidate.getApplications());

        //ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        //HttpSession httpSession = attributes.getRequest().getSession();


        return "candidate/show";
    }
}
