package com.example.summerprojecttest.bootstrap;

import com.example.summerprojecttest.model.Application;
import com.example.summerprojecttest.model.Candidate;
import com.example.summerprojecttest.model.Job;
import com.example.summerprojecttest.repo.ApplicationRepository;
import com.example.summerprojecttest.repo.CandidateRepository;
import com.example.summerprojecttest.repo.JobRepository;
import com.example.summerprojecttest.services.CandidateServiceImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private CandidateRepository candidateRepository;
    private ApplicationRepository applicationRepository;
    private JobRepository jobRepository;

    public DevBootstrap(CandidateRepository candidateRepository, ApplicationRepository applicationRepository, JobRepository jobRepository) {
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    private void initData() {

        Candidate hasan = new Candidate("Hasan", "Üstün", 22, "İstanbul", "Yeditepe");
        Candidate aleyna = new Candidate("Aleyna", "Üstün", 20, "İstanbul", "Yeditepe");
        hasan.setUserName("hasbey");
        aleyna.setUserName("aleyna");

        Application app1 = new Application(LocalTime.now());
        hasan.getApplications().add(app1);
        app1.setApplicant(hasan);

        System.out.println(Timestamp.valueOf(LocalDateTime.now()));
        Job job = new Job("Software Engineer", "This is a software engineering job posting.",
                LocalDate.now(), LocalDate.now(), Job.Status.ACTIVE, "HasComp", "İstanbul");
        Job job2 = new Job("Software Architect", "This is a software architecture job posting.",
                LocalDate.now(), LocalDate.now(), Job.Status.ACTIVE, "Company2", "İstanbul");
        Job job3 = new Job("Web Developer", "This is a web development job posting.",
                LocalDate.now(), LocalDate.now(), Job.Status.ACTIVE, "TComp", "Ankara");
        job.getApplications().add(app1);
        app1.setJob(job);

        candidateRepository.save(hasan);
        candidateRepository.save(aleyna);

        jobRepository.save(job);
        jobRepository.save(job2);
        jobRepository.save(job3);

        applicationRepository.save(app1);

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

        Application app2 = new Application(LocalTime.now());
        app2.setApplicant(hasan);
        applicationRepository.save(app2);

        Application app3 = new Application(LocalTime.now());
        app3.setApplicant(hasan);
        applicationRepository.save(app3);

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
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }
}
