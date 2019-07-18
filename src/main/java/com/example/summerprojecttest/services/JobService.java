package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.Job;

import java.time.LocalDate;
import java.util.Set;

public interface JobService extends BaseService<Job, Integer> {
    Set<Job> findAllByOrderByActivationTimeDesc();
    Set<Job> findAllByOrderByTitleAsc();

}
