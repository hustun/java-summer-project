package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.BlacklistEntry;

public interface BlacklistEntryService extends BaseService<BlacklistEntry, Integer> {

    BlacklistEntry findByCandidateId(Integer id);

}
