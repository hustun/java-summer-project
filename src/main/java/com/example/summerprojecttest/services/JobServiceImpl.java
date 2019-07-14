package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.Job;
import com.example.summerprojecttest.repo.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Service
public class JobServiceImpl implements JobService {

    JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Set<Job> findAll() {
        Set<Job> jobs = new HashSet<>();

        jobRepository.findAll().forEach(jobs::add);

        return jobs;
    }

    @Override
    public Job findById(Integer id) {
        Optional<Job> optionalJob = jobRepository.findById(id);

        if(optionalJob.isPresent()){
            return optionalJob.get();
        }else{
            return null;
        }
    }

    @Override
    public Job save(Job object) {
        return jobRepository.save(object);
    }

    @Override
    public void delete(Job object) {
        jobRepository.delete(object);
    }

    @Override
    public void deleteById(Integer id) {
        jobRepository.deleteById(id);
    }

    @Override
    public Set<Job> findAllByOrderByActivationTimeDesc() {
        return jobRepository.findAllByOrderByActivationTimeDesc();
    }
}
