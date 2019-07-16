package com.example.summerprojecttest.repo;

import com.example.summerprojecttest.model.Skill;
import org.springframework.data.repository.CrudRepository;

public interface SkillRepository extends CrudRepository<Skill, Integer> {
    Skill findBySkillName(String skillName);
}
