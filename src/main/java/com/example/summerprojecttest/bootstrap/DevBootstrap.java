package com.example.summerprojecttest.bootstrap;

import com.example.summerprojecttest.model.*;
import com.example.summerprojecttest.repo.*;
import com.example.summerprojecttest.services.CandidateServiceImpl;
import com.example.summerprojecttest.services.SkillService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private CandidateRepository candidateRepository;
    private ApplicationRepository applicationRepository;
    private JobRepository jobRepository;
    private SkillRepository skillRepository;
    private BlacklistEntryRepository blacklistEntryRepository;

    public DevBootstrap(CandidateRepository candidateRepository, ApplicationRepository applicationRepository, JobRepository jobRepository, SkillRepository skillRepository, BlacklistEntryRepository blacklistEntryRepository) {
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.blacklistEntryRepository = blacklistEntryRepository;
    }

    private void initData() {

        ArrayList<Skill> skills = SkillsList.getSkillsList();


        Candidate hasan = new Candidate("Hasan", "Üstün", 22, "İstanbul", "Yeditepe");
        Candidate aleyna = new Candidate("Has", "Has", 20, "İstanbul", "Yeditepe");
        Candidate darkHasan = new Candidate("Kötü", "Hasan", 21, "İstanbul", "Yeditepe");

        hasan.setUserName("hasbey");
        aleyna.setUserName("has");
        hasan.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        hasan.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        hasan.getSkills().add(skills.get(new Random().nextInt(skills.size())));

        aleyna.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        aleyna.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        aleyna.getSkills().add(skills.get(new Random().nextInt(skills.size())));

        for ( Skill skill : skills ){
            skillRepository.save(skill);
        }

        //hasan.setEmail("hasan.ustun@std.yeditepe.edu.tr");



        System.out.println(Timestamp.valueOf(LocalDateTime.now()));
        Job job = new Job("Software Engineer", "This is a software engineering job posting.",
                LocalDate.now(), LocalDate.now(), Job.Status.ACTIVE, "HasComp", "İstanbul");
        Job job2 = new Job("Software Architect", "This is a software architecture job posting.",
                LocalDate.now(), LocalDate.now(), Job.Status.ACTIVE, "Company2", "İstanbul");
        Job job3 = new Job("Web Developer", "This is a web development job posting.",
                LocalDate.now(), LocalDate.now(), Job.Status.ACTIVE, "TComp", "Ankara");
        Job job4 = new Job("Web Designer", "This is a web design job posting.",
                LocalDate.now(), LocalDate.now(), Job.Status.ACTIVE, "CompanyNull", "Seoul");
        Job job5 = new Job("Fullstack Developer", "This is a fullstack development job posting.",
                LocalDate.now(), LocalDate.now(), Job.Status.ACTIVE, "MyCompany", "Los Angeles");

        job.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        job.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        job.getSkills().add(skills.get(new Random().nextInt(skills.size())));

        job2.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        job2.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        job2.getSkills().add(skills.get(new Random().nextInt(skills.size())));

        job3.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        job3.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        job3.getSkills().add(skills.get(new Random().nextInt(skills.size())));

        job4.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        job4.getSkills().add(skills.get(new Random().nextInt(skills.size())));

        job5.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        job5.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        job5.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        job5.getSkills().add(skills.get(new Random().nextInt(skills.size())));
        job5.getSkills().add(skills.get(new Random().nextInt(skills.size())));


        candidateRepository.save(hasan);
        candidateRepository.save(aleyna);
        candidateRepository.save(darkHasan);

        jobRepository.save(job);
        jobRepository.save(job2);
        jobRepository.save(job3);
        jobRepository.save(job4);
        jobRepository.save(job5);

        CandidateServiceImpl candidateService = new CandidateServiceImpl(candidateRepository);

        System.out.println(candidateService.findById(1).toString());
        Set<Candidate> results = candidateService.findByLastName("Üstün");
        for(Candidate candidate : results){
            System.out.println(candidate.toString());
        }


        /*
        Optional<Candidate> o = candidateRepository.findById(1);
        if(o.isPresent() ){
            Candidate candidate = (Candidate) o.get();
            System.out.println(candidate.toString());
        }
        */

        //applicationRepository.delete(app1);

        Application app1 = new Application(LocalTime.now());
        hasan.getApplications().add(app1);
        app1.setApplicant(hasan);
        app1.setJob(job);
        job.getApplications().add(app1);
        applicationRepository.save(app1);

        Application app2 = new Application(LocalTime.now());
        app2.setApplicant(hasan);
        hasan.getApplications().add(app2);
        app2.setJob(job3);
        job3.getApplications().add(app2);
        applicationRepository.save(app2);

        Application app3 = new Application(LocalTime.now());
        app3.setApplicant(aleyna);
        aleyna.getApplications().add(app3);
        app3.setJob(job2);
        job2.getApplications().add(app3);
        applicationRepository.save(app3);

        Application app4 = new Application(LocalTime.now());
        app4.setApplicant(aleyna);
        aleyna.getApplications().add(app4);
        app4.setJob(job4);
        job4.getApplications().add(app4);
        applicationRepository.save(app4);

        Application app5 = new Application(LocalTime.now());
        app5.setApplicant(hasan);
        hasan.getApplications().add(app5);
        app5.setJob(job5);
        job5.getApplications().add(app5);
        applicationRepository.save(app5);

        Application app6 = new Application(LocalTime.now());
        app6.setApplicant(darkHasan);
        darkHasan.getApplications().add(app6);
        app6.setJob(job5);
        job5.getApplications().add(app6);
        applicationRepository.save(app6);

        Iterable<Application> applicationIterable = applicationRepository.findAll();
        Set<Application> applications = new HashSet<>();
        for(Application app : applicationIterable){
            applications.add(app);
        }

        for(Application app : applications){
            System.out.println("----------------------");
            System.out.println(app.toString());
            System.out.println("----------------------");

        }
        BlacklistEntry blacklistEntry = new BlacklistEntry(darkHasan, "Çünkü kötü.");
        blacklistEntryRepository.save(blacklistEntry);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }
}
