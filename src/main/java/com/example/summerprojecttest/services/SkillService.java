package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.Skill;

public interface SkillService extends BaseService<Skill, Integer> {
    Skill findBySkillName(String skillName);
}
