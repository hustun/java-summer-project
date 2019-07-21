package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.model.*;
import com.example.summerprojecttest.repo.JobRepository;
import com.example.summerprojecttest.services.ApplicationService;
import com.example.summerprojecttest.services.CandidateService;
import com.example.summerprojecttest.services.JobService;
import com.example.summerprojecttest.services.SkillService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class JobController {

    private JobService jobService;
    private SkillService skillService;
    private CandidateService candidateService;
    private ApplicationService applicationService;
    private ArrayList<Skill> skills = new ArrayList<>();

    public JobController(JobService jobService, SkillService skillService, CandidateService candidateService, ApplicationService applicationService) {
        this.jobService = jobService;
        this.skillService = skillService;
        this.candidateService = candidateService;
        this.applicationService = applicationService;
        this.skills = SkillsList.getSkillsList();
    }

    @RequestMapping("/jobs")
    public String showJobsPage(@RequestParam(required = false, value = "sortBy") String sortBy, Model model){

        model.addAttribute("candidate", getCandidate());

        if (sortBy == null){
            sortBy = "date";
        }
        switch (sortBy){
            case "date":
                model.addAttribute("jobs", jobService.findAllByOrderByActivationTimeDesc());
                model.addAttribute("sort", 0);
                break;
            case "name":
                model.addAttribute("jobs", jobService.findAllByOrderByTitleAsc());
                model.addAttribute("sort", 1);
                break;
            case "relevance":
                if( getCandidate() != null){
                    Set<Job> availableJobs = jobService.findAll();
                    Candidate candidate = getCandidate();
                    Map<Job, Double> jobMap = new HashMap<>();
                    for (Job job : availableJobs){
                        System.out.println(candidate.matchPercent(job));
                        jobMap.put(job,candidate.matchPercent(job));
                    }
                    Map<Job, Double> sortedMap = jobMap.entrySet().stream()
                            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                    sortedMap.entrySet().forEach(entry->{System.out.println(entry.getKey().getTitle() + " " + entry.getValue());});

                    ArrayList<Double> percents = new ArrayList<>();
                    for (Job job : sortedMap.keySet()){
                        Double skillMatchCount = 0D;
                        for(Skill skill : candidate.getSkills()){
                            if (SkillsList.contains(job.getSkills(), skill)){
                                skillMatchCount++;
                            }
                        }
                        percents.add(skillMatchCount/job.getSkills().size());

                    }
                    model.addAttribute("percents", percents);

                    model.addAttribute("jobs", sortedMap.keySet());
                    model.addAttribute("sort", 2);
                }
                break;
        }
        return "jobs";
    }

    @GetMapping("/jobs/newJobForm")
    public String newJobForm(Model model){
        Job job = new Job();

        model.addAttribute("job", job);
        model.addAttribute("skills", skills);

        return "jobs/new";
    }

    @PostMapping("/jobs/new")
    public String addJob(@ModelAttribute("job") Job job, @RequestParam(value = "jobSkill", required = false) List<String> skills){

        if(skills != null){
            for (String skillName : skills){
                job.getSkills().add(skillService.findBySkillName(skillName));
                System.out.println("Job Controller Skill: " + skillName);
            }
        }

        jobService.save(job);

        return "redirect:/jobs";
    }

    @RequestMapping("/jobs/show/{id}")
    public String showJobsById(@PathVariable String id, Model model){
        Job job = jobService.findById(Integer.valueOf(id));
        model.addAttribute("job", job);
        model.addAttribute("skills", job.getSkills());
        model.addAttribute("applications", job.getApplications());


        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attributes.getRequest().getSession();

        if ( httpSession.getAttribute("isCandidate") != null){
            if ((boolean) httpSession.getAttribute("isCandidate") ){
                Candidate candidate = candidateService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
                model.addAttribute("candidate", candidate);

                Integer skillMatchCount = 0;
                for (Skill skill : candidate.getSkills()){
                    if (SkillsList.contains(job.getSkills(), skill)){
                        skillMatchCount++;
                    }
                }

                model.addAttribute("skillMatchCount", skillMatchCount);
                model.addAttribute("skillCount", job.getSkills().size());

            }
        }
        return "jobs/show";
    }

    @RequestMapping("/jobs/show")
    public String showSingleJobPage(Model model){
        return "jobs/show";
    }

    @GetMapping("/jobs/updateJob")
    public String updateJob(@RequestParam("jobId") Integer id, Model model){
        Job job = jobService.findById(id);

        Set<Skill> skillsList = job.getSkills();

        for (Skill skill : skillsList){
            System.out.println("Job Skill: " + skill.getSkillName() + " " + skill.getId());
        }

        for (Skill skill : skills){
            System.out.println("Skill: " + skill.getSkillName() + " " + skill.getId());
            System.out.println(skillService.findBySkillName("C").equals(skill));
        }

        for (Skill skill : skills){
            System.out.println("Contains " + skill.getSkillName() + " : " + SkillsList.contains(skillsList, skill));
        }


        model.addAttribute("job", job);
        model.addAttribute("skills", skills);

        return "jobs/new";
    }

    //@GetMapping("jobs/{jobId}/apply")
    //@PreAuthorize("has")

    @RequestMapping("/jobs/apply/{jobId}/{candidateId}")
    public String applyJob(@PathVariable String jobId, @PathVariable String candidateId, Model model){
        Job job = jobService.findById(Integer.valueOf(jobId));
        Candidate candidate = candidateService.findById(Integer.valueOf(candidateId));

        Application application = new Application(LocalTime.now());
        application.setJob(job);
        application.setApplicant(candidate);

        job.getApplications().add(application);
        candidate.getApplications().add(application);

        applicationService.save(application);

        //ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        //HttpSession httpSession = attributes.getRequest().getSession();


        return "index";
    }

    private Candidate getCandidate(){
        Candidate candidate = candidateService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return candidate;
    }


}