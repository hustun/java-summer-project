package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.BlacklistEntry;
import com.example.summerprojecttest.repo.BlacklistEntryRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Service
public class BlacklistEntryServiceImpl implements BlacklistEntryService {

    private BlacklistEntryRepository blacklistEntryRepository;

    public BlacklistEntryServiceImpl(BlacklistEntryRepository blacklistEntryRepository) {
        this.blacklistEntryRepository = blacklistEntryRepository;
    }

    @Override
    public Set<BlacklistEntry> findAll() {
        Set<BlacklistEntry> blacklistEntries = new HashSet<>();

        blacklistEntryRepository.findAll().forEach(blacklistEntries::add);

        return blacklistEntries;
    }

    @Override
    public BlacklistEntry findById(Integer id) {
        Optional<BlacklistEntry> optionalBlacklistEntry = blacklistEntryRepository.findById(id);

        if (optionalBlacklistEntry.isPresent()){
            return optionalBlacklistEntry.get();
        }else{
            return null;
        }
    }

    @Override
    public BlacklistEntry save(BlacklistEntry object) {
        return blacklistEntryRepository.save(object);
    }

    @Override
    public void delete(BlacklistEntry object) {
        blacklistEntryRepository.delete(object);
    }

    @Override
    public void deleteById(Integer id) {
        blacklistEntryRepository.deleteById(id);
    }

    @Override
    public BlacklistEntry findByCandidateId(Integer id) {
        return blacklistEntryRepository.findByCandidateId(id);
    }
}
