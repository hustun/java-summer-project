package com.example.summerprojecttest.repo;

import com.example.summerprojecttest.model.Job;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Set;

public interface JobRepository extends CrudRepository<Job, Integer> {
    Set<Job> findAllByOrderByActivationTimeDesc();
}
