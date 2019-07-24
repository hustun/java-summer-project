package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.model.*;
import com.example.summerprojecttest.repo.JobRepository;
import com.example.summerprojecttest.services.*;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class JobController {

    private JobService jobService;
    private SkillService skillService;
    private CandidateService candidateService;
    private ApplicationService applicationService;
    private BlacklistEntryService blacklistEntryService;
    private ArrayList<Skill> skills = new ArrayList<>();
    @PersistenceContext
    private EntityManager entityManager;

    public JobController(JobService jobService, SkillService skillService, CandidateService candidateService,
                         ApplicationService applicationService, BlacklistEntryService blacklistEntryService) {
        this.jobService = jobService;
        this.skillService = skillService;
        this.candidateService = candidateService;
        this.applicationService = applicationService;
        this.blacklistEntryService = blacklistEntryService;
        this.skills = SkillsList.getSkillsList();
    }

    @RequestMapping("/jobs")
    public String showJobsPage(@RequestParam(required = false, value = "sortBy") String sortBy, Model model){

        //check job status change
        for (Job job : jobService.findAll()){
            if(job.getStatus() == Job.Status.ACTIVE){
                if( job.getClosingTime() != null && job.getClosingTime().isBefore(LocalDateTime.now())){
                    job.setStatus(Job.Status.INACTIVE);
                    job.setActivationTime(null);
                    job.setClosingTime(null);
                    jobService.save(job);
                }
            }
            else if (job.getStatus() == Job.Status.INACTIVE){
                if( job.getActivationTime() != null && job.getActivationTime().isBefore(LocalDateTime.now())){
                    job.setStatus(Job.Status.ACTIVE);
                    job.setActivationTime(null);
                    job.setClosingTime(null);
                    jobService.save(job);
                }
            }

        }

        model.addAttribute("candidate", getCandidate());

        //check if blacklisted
        if(getCandidate() != null){
            boolean isBlacklisted = true;
            if (blacklistEntryService.findByCandidateId(getCandidate().getId()) == null){
                isBlacklisted = false;
            }
            else{
                model.addAttribute("reason", blacklistEntryService.findByCandidateId((getCandidate().getId())).getReason());
            }
            model.addAttribute("isBlacklisted", isBlacklisted);
        }

        setActiveDuration();

        //return jobs depending on sorting criteria
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
                        percents.add( Math.floor( (skillMatchCount/(job.getSkills().size())) *100));

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

        //set job skills
        if(skills != null){
            for (String skillName : skills){
                job.getSkills().add(skillService.findBySkillName(skillName));
                System.out.println("Job Controller Skill: " + skillName);
            }
        }

        //set job creation time
        if(job.getCreationTime() == null){
            job.setCreationTime(LocalDateTime.now());
        }
        jobService.save(job);

        return "redirect:/jobs";
    }

    @RequestMapping("/jobs/show/{id}")
    public String showJobsById(@PathVariable String id, Model model){
        Job job = jobService.findById(Integer.valueOf(id));
        job.updateDuration();
        model.addAttribute("job", job);
        model.addAttribute("skills", job.getSkills());
        model.addAttribute("applications", job.getApplications());
        model.addAttribute("candidateObject", getCandidate());


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

        if (getCandidate() == null || blacklistEntryService.findByCandidateId(getCandidate().getId()) != null){
            return "redirect:/";
        }
        if (getCandidate() != null && getCandidate().isApplied(job)){
            return "redirect:/jobs";
        }

        Application application = new Application(LocalDateTime.now());
        application.setJob(job);
        application.setApplicant(candidate);

        job.getApplications().add(application);
        candidate.getApplications().add(application);

        applicationService.save(application);
        model.addAttribute("candidateObject", candidate);

        //ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        //HttpSession httpSession = attributes.getRequest().getSession();


        return "redirect:/jobs";
    }

    @PostMapping("/jobs/search")
    public String searchJob(@RequestParam(value = "firstText") String firstText, @RequestParam(value = "sortBy") String sortBy, Model model){

        System.out.println(firstText + " " + sortBy);

        if (sortBy.equals("candidate")){
            List<Candidate> searchResults = (List<Candidate>) searchResults(firstText, sortBy);
            for (Candidate candidate : searchResults){
                System.out.println(candidate.toString());
            }
            model.addAttribute("candidates", searchResults);
            model.addAttribute("candidateObject", getCandidate());

            return "candidates";
        }
        else if (sortBy.equals("job")){
            model.addAttribute("candidate", getCandidate());

            if(getCandidate() != null){
                boolean isBlacklisted = true;
                if (blacklistEntryService.findByCandidateId(getCandidate().getId()) == null){
                    isBlacklisted = false;
                }
                model.addAttribute("isBlacklisted", isBlacklisted);
            }

            List<Job> searchResults = (List<Job>) searchResults(firstText, sortBy);
            for (Job job : searchResults){
                System.out.println(job.toString());
            }
            for (Job job : searchResults){
                job.updateDuration();
            }
            model.addAttribute("jobs", searchResults);
            model.addAttribute("sort", 0);

            return "jobs";

        }

        return "redirect:/jobs";
    }

    @RequestMapping("/jobs/applications/{id}")
    public String showJobApplications(@PathVariable String id, @RequestParam(required = false, value = "filter") String filter, Model model) {
        Job job = jobService.findById(Integer.valueOf(id));
        model.addAttribute("job", job);
        model.addAttribute("candidateObject", getCandidate());

        if (filter == null) {
            filter = "all";
        }

        switch (filter) {
            case "all":
                model.addAttribute("applications", job.getApplications());
                model.addAttribute("filter", 0);
                break;
            case "accepted":
                model.addAttribute("applications", applicationService.findByStatusAndJob(StatusType.ACCEPTED, job));
                model.addAttribute("filter", 1);
                break;
            case "rejected":
                model.addAttribute("applications", applicationService.findByStatusAndJob(StatusType.REJECTED, job));
                model.addAttribute("filter", 2);
                break;
            case "inProcess":
                model.addAttribute("applications", applicationService.findByStatusAndJob(StatusType.IN_PROCESS, job));
                model.addAttribute("filter", 3);
                break;
            case "pending":
                model.addAttribute("applications", applicationService.findByStatusAndJob(StatusType.PENDING, job));
                model.addAttribute("filter", 4);
                break;
        }
        return "jobs/applications";
    }

    private Candidate getCandidate(){
        Candidate candidate = candidateService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return candidate;
    }

    private void setActiveDuration() {
        for (Job job : jobService.findAll()){
            if (job.getActivationTime() != null){
                job.updateDuration();
            }
        }
    }

    private List<?> searchResults(String searchTerm, String searchBy){
        entityManager.getEntityManagerFactory().createEntityManager();
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager(entityManager);

        if (searchBy.equals("candidate")){

            QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                    .buildQueryBuilder()
                    .forEntity(Candidate.class)
                    .overridesForField("firstName", "customanalyzer_query")
                    .overridesForField("lastName", "customanalyzer_query")
                    .overridesForField("bio", "customanalyzer_query")
                    .overridesForField("university", "customanalyzer_query")
                    .overridesForField("skills.skillName", "customanalyzer_query")
                    .get();

            org.apache.lucene.search.Query query = queryBuilder
                    .simpleQueryString()
                    .onFields("firstName", "lastName", "bio", "university", "skills.skillName")
                    .matching(searchTerm)
                    .createQuery();

            org.hibernate.search.jpa.FullTextQuery jpaQuery
                    = fullTextEntityManager.createFullTextQuery(query, Candidate.class);

            System.out.println("Results: " + jpaQuery.getResultSize());
            List<Candidate> results = jpaQuery.getResultList();
            return results;
        }else if (searchBy.equals("job")){

            QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                    .buildQueryBuilder()
                    .forEntity(Job.class)
                    .overridesForField("title", "customanalyzer_query")
                    .overridesForField("description", "customanalyzer_query")
                    .overridesForField("companyName", "customanalyzer_query")
                    .overridesForField("location", "customanalyzer_query")
                    .overridesForField("skills.skillName", "customanalyzer_query")
                    .get();

            org.apache.lucene.search.Query query = queryBuilder
                    .simpleQueryString()
                    .onFields("title", "description", "companyName", "location", "skills.skillName")
                    .matching(searchTerm)
                    .createQuery();

            org.hibernate.search.jpa.FullTextQuery jpaQuery
                    = fullTextEntityManager.createFullTextQuery(query, Job.class);

            System.out.println("Results: " + jpaQuery.getResultSize());
            List<Job> results = jpaQuery.getResultList();
            return results;
        }else{
            return null;
        }
    }


}