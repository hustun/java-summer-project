package com.example.summerprojecttest.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "languageName")
    @NotEmpty
    private String languageName;

    public Language() {
    }

    public Language(@NotEmpty String languageName) {
        this.languageName = languageName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Language){
            if(((Language) obj).getLanguageName().equals(this.languageName)){
                return true;
            }
        }
        return false;
    }
}
