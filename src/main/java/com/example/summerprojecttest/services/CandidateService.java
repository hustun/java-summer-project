package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.Application;
import com.example.summerprojecttest.model.Candidate;

import java.util.Set;

public interface CandidateService extends BaseService<Candidate, Integer>{

    Set<Candidate> findByLastName(String lastName);

    Candidate findByUserName(String userName);
}
