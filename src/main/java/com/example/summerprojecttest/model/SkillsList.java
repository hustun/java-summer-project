package com.example.summerprojecttest.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SkillsList {

    private static ArrayList<Skill> skillsList = new ArrayList<>();

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

    public static boolean contains(ArrayList<Skill> skills, Skill skill){
        for (Skill s : skills){
            if (s.equals(skill)){
                return true;
            }
        }
        return false;
    }

    public static boolean contains(Set<Skill> skills, Skill skill){
        for (Skill s : skills){
            if (s.equals(skill)){
                return true;
            }
        }
        return false;
    }
}
