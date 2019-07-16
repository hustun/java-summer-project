package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.Skill;
import com.example.summerprojecttest.repo.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Service
public class SkillServiceImpl implements SkillService {

    private SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Set<Skill> findAll() {
        Set<Skill> skills = new HashSet<>();

        skillRepository.findAll().forEach(skills::add);

        return skills;
    }

    @Override
    public Skill findById(Integer id) {
        Optional<Skill> optionalSkill = skillRepository.findById(id);

        if (optionalSkill.isPresent()){
            return optionalSkill.get();
        }else{
            return null;
        }
    }

    @Override
    public Skill save(Skill object) {
        return skillRepository.save(object);
    }

    @Override
    public void delete(Skill object) {
        skillRepository.delete(object);
    }

    @Override
    public void deleteById(Integer id) {
        skillRepository.deleteById(id);
    }

    @Override
    public Skill findBySkillName(String skillName) {
        return skillRepository.findBySkillName(skillName);
    }
}
