package com.example.summerprojecttest.model;

import com.example.summerprojecttest.services.CandidateServiceImpl;

import javax.persistence.Entity;
import java.util.ArrayList;

public class Blacklist {

    private static ArrayList<BlacklistEntry> blacklist = new ArrayList<>();


    public static ArrayList<BlacklistEntry> getBlacklist() {

            return blacklist;
    }

    public static boolean contains(Candidate candidate){
        for (BlacklistEntry blacklistEntry : blacklist){
            if (blacklistEntry.getCandidate().equals(candidate)){
                return true;
            }
        }
        return false;
    }
}
