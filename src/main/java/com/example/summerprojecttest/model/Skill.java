package com.example.summerprojecttest.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class Skill{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "skillName")
    @NotEmpty
    private String skillName;

    private String skillDesc;
    private String skillCategory;

    public Skill() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Skill(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Skill){
            if(((Skill) obj).getSkillName().equals(this.skillName)){
                return true;
            }
        }
        return false;
    }
}
