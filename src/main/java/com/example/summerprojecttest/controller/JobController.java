package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.model.Job;
import com.example.summerprojecttest.repo.JobRepository;
import com.example.summerprojecttest.services.JobService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class JobController {

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @RequestMapping("/jobs")
    public String showJobsPage(Model model){
        model.addAttribute("jobs", jobService.findAllByOrderByActivationTimeDesc());
        model.addAttribute("status", "status");
        return "jobs";
    }

    @GetMapping("/jobs/newJobForm")
    public String newJobForm(Model model){
        Job job = new Job();

        model.addAttribute("job", job);

        return "jobs/new";
    }

    @PostMapping("/jobs/new")
    public String addJob(@ModelAttribute("job") Job job){
        System.out.println(job.getActivationTime() + " ------- " + job.getClosingTime());
        jobService.save(job);

        return "redirect:/jobs";
    }

    @RequestMapping("/jobs/show/{id}")
    public String showJobsById(@PathVariable String id, Model model){
        model.addAttribute("job", jobService.findById(new Integer(id)));

        return "jobs/show";
    }

    @RequestMapping("/jobs/show")
    public String showSingleJobPage(Model model){
        return "jobs/show";
    }

    @GetMapping("/jobs/updateJob")
    public String updateJob(@RequestParam("jobId") Integer id, Model model){
        Job job = jobService.findById(id);

        model.addAttribute("job", job);

        return "jobs/new";
    }

    //@GetMapping("jobs/{jobId}/apply")
    //@PreAuthorize("has")
}
