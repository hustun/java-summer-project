package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.Application;
import com.example.summerprojecttest.model.Candidate;
import com.example.summerprojecttest.model.Job;
import com.example.summerprojecttest.model.StatusType;
import org.springframework.stereotype.Service;

import java.util.Set;

public interface ApplicationService extends BaseService<Application, Integer> {

    Set<Application> findByStatus(StatusType status);
    Set<Application> findByStatusAndApplicant(StatusType statusType, Candidate candidate);
    Set<Application> findByStatusAndJob(StatusType statusType, Job job);

}
