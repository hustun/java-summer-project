package com.example.summerprojecttest.repo;

import com.example.summerprojecttest.model.Candidate;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CandidateRepository extends CrudRepository<Candidate, Integer> {

    Set<Candidate> findByLastName(String lastName);

    Candidate findByUserName(String userName);
}
