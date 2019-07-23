package com.example.summerprojecttest.repo;

import com.example.summerprojecttest.model.Blacklist;
import com.example.summerprojecttest.model.BlacklistEntry;
import org.springframework.data.repository.CrudRepository;

public interface BlacklistEntryRepository extends CrudRepository<BlacklistEntry, Integer> {

    BlacklistEntry findByCandidateId(Integer id);
}
