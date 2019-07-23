package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.model.*;
import com.example.summerprojecttest.services.BlacklistEntryService;
import com.example.summerprojecttest.services.CandidateService;
import com.example.summerprojecttest.services.LanguageService;
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
    private LanguageService languageService;
    private BlacklistEntryService blacklistEntryService;
    private ArrayList<Skill> skills = new ArrayList<>();


    public CandidateController(CandidateService candidateService, SkillService skillService,
                               BlacklistEntryService blacklistEntryService, LanguageService languageService) {
        this.candidateService = candidateService;
        this.skillService = skillService;
        this.blacklistEntryService = blacklistEntryService;
        this.languageService = languageService;
        this.skills = SkillsList.getSkillsList();
    }

    @RequestMapping("/candidates")
    public String showCandidatesPage(Model model){
        model.addAttribute("candidates", candidateService.findAll());
        model.addAttribute("candidateObject", getCandidate());
        return "candidates";
    }

    @GetMapping("/candidate/newCandidateForm")
    public String newCandidate(Model model){
        RedirectView redirectView = new RedirectView();
        Candidate candidate = new Candidate();

        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        candidate.setEmail(email);

        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attributes.getRequest().getSession();
        candidate.setFirstName(httpSession.getAttribute("firstName").toString());
        candidate.setLastName(httpSession.getAttribute("lastName").toString());
        candidate.setPhoto(httpSession.getAttribute("photo").toString());

        System.out.println("Photo: " + candidate.getPhoto());

        model.addAttribute("candidate", candidate);
        model.addAttribute("skills", skills);
        model.addAttribute("languages", LanguageList.getLanguageList());

        return "candidate/new";
    }

    @PostMapping("/candidate/new")
    public String addCandidate(@ModelAttribute("candidate") Candidate candidate,
                               @RequestParam(value = "candidateSkill", required = false) List<String> skills,
                               @RequestParam(value = "candidateLanguage", required = false) List<String> languages){

        if(skills != null){
            System.out.println("List length :" +  skills.size());
            for (String skillName : skills){
                candidate.getSkills().add(skillService.findBySkillName(skillName));
            }
        }

        if(languages != null){
            System.out.println("List length :" +  languages.size());
            for (String languageName : languages){
                candidate.getLanguages().add(languageService.findByLanguageName(languageName));
            }
        }

        System.out.println("Photo: " + candidate.getPhoto());
        candidateService.save(candidate);


        return "redirect:/jobs";
    }

    @RequestMapping("/candidate/show/{id}")
    public String showCandidateById(@PathVariable String id, Model model){
        Candidate candidate = candidateService.findById(Integer.valueOf(id));
        model.addAttribute("candidate", candidate);
        model.addAttribute("skills", candidate.getSkills());
        model.addAttribute("languages", candidate.getLanguages());
        model.addAttribute("applications", candidate.getApplications());
        model.addAttribute("candidateObject", getCandidate());

        System.out.println("Photo: " + candidate.getPhoto());

        //ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        //HttpSession httpSession = attributes.getRequest().getSession();


        return "candidate/show";
    }

    @RequestMapping("/candidate/applications/{id}")
    public String showCandidateApplications(@PathVariable String id, Model model){
        Candidate candidate = candidateService.findById(Integer.valueOf(id));
        model.addAttribute("candidate", candidate);
        model.addAttribute("applications", candidate.getApplications());
        model.addAttribute("candidateObject", getCandidate());


        //ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        //HttpSession httpSession = attributes.getRequest().getSession();


        return "candidate/applications";
    }

    @PostMapping("candidate/blacklist/{id}")
    public String blacklistCandidate(@PathVariable String id, @RequestParam(value = "reasonText") String reasonText){
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attributes.getRequest().getSession();

        //only hr has permission to access
        if(httpSession.getAttribute("isHR") == null){
            return "redirect:/";
        }
        Candidate candidate = candidateService.findById(Integer.valueOf(id));

        //duplicate entries
        if (blacklistEntryService.findByCandidateId(Integer.valueOf(id)) != null){
            return "redirect:/";
        }

        BlacklistEntry blacklistEntry = new BlacklistEntry(candidate, reasonText);

        for ( Application application: candidate.getApplications()){
            application.setStatus(StatusType.REJECTED);
        }
        blacklistEntryService.save(blacklistEntry);

        return "redirect:/";
    }

    private Candidate getCandidate(){
        Candidate candidate = candidateService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return candidate;
    }

}
