package com.example.summerprojecttest.model;

import com.example.summerprojecttest.services.BlacklistEntryService;
import com.example.summerprojecttest.services.CandidateServiceImpl;

import javax.persistence.Entity;
import java.util.ArrayList;

public class Blacklist {

    private BlacklistEntryService blacklistEntryService;

    public Blacklist(BlacklistEntryService blacklistEntryService) {
        this.blacklistEntryService = blacklistEntryService;
    }

    /*private static ArrayList<BlacklistEntry> blacklist = new ArrayList<>();


    public static ArrayList<BlacklistEntry> getBlacklist() {

            return blacklist;
    }*/

    /*public static boolean contains(Candidate candidate){
        return false;
    }*/
}
