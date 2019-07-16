package com.example.summerprojecttest.model;

import java.util.ArrayList;

public class SkillsList {

    private static ArrayList<Skill> skillsList = new ArrayList<Skill>();

    public static ArrayList<Skill> getSkillsList() {
        if (skillsList.size() == 0){
            skillsList.add(new Skill("HTML"));
            skillsList.add(new Skill("CSS"));
            skillsList.add(new Skill("JS"));
            skillsList.add(new Skill("C"));
            skillsList.add(new Skill("C++"));
            skillsList.add(new Skill("C#"));
            skillsList.add(new Skill("Java"));
            skillsList.add(new Skill("Python"));
            skillsList.add(new Skill("Microsoft Office"));
            skillsList.add(new Skill("Matlab"));

        }
        return skillsList;
    }
}
