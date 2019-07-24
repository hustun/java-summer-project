package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.Candidate;
import com.example.summerprojecttest.repo.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public Set<Candidate> findByLastName(String lastName) {
        return candidateRepository.findByLastName(lastName);
    }

    @Override
    public Candidate findByEmail(String email) {
        return candidateRepository.findByEmail(email);
    }


    @Override
    public Candidate findById(Integer id) {
        Optional<Candidate> optionalCandidate = candidateRepository.findById(id);

        if(optionalCandidate.isPresent()){
            return optionalCandidate.get();
        }else{
            return null;
        }
    }

    @Override
    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public Set<Candidate> findAll() {
        Set<Candidate> candidates = new HashSet<>();

        candidateRepository.findAll().forEach(candidates::add);

        return candidates;
    }

    @Override
    public void delete(Candidate candidate) {
        candidateRepository.delete(candidate);
    }

    @Override
    public void deleteById(Integer id) {
        candidateRepository.deleteById(id);
    }

}
