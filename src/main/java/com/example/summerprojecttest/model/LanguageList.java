package com.example.summerprojecttest.model;

import java.util.ArrayList;
import java.util.Set;

public class LanguageList {

    private static ArrayList<Language> languageList = new ArrayList<>();

    public static ArrayList<Language> getLanguageList() {
        if (languageList.size() == 0){
            languageList.add(new Language("English"));
            languageList.add(new Language("Spanish"));
            languageList.add(new Language("French"));
            languageList.add(new Language("German"));
            languageList.add(new Language("Turkish"));
            languageList.add(new Language("Chinese"));
            languageList.add(new Language("Korean"));

        }
        return languageList;
    }

    public static boolean contains(ArrayList<Language> languages, Language language){
        for (Language l : languages){
            if (l.equals(language)){
                return true;
            }
        }
        return false;
    }

    public static boolean contains(Set<Language> languages, Language language){
        for (Language l : languages){
            if (l.equals(language)){
                return true;
            }
        }
        return false;
    }
}
