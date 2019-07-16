package com.example.summerprojecttest.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userName")
    //@NotEmpty
    private String userName;

    @Column(name = "firstName")
    @NotEmpty
    private String firstName;

    @Column(name = "lastName")
    @NotEmpty
    private String lastName;

    @Column(name = "age")
    @NotNull
    private Integer age;

    @Column(name = "address")
    @NotEmpty
    private String address;

    @Column(name = "university")
    @NotEmpty
    private String university;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "applicant")
    private Set<Application> applications = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "candidate_skills",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public Candidate() {
    }

    public Candidate(@NotEmpty String firstName, @NotEmpty String lastName, @NotNull Integer age, @NotEmpty String address, @NotEmpty String university) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
        this.university = university;
    }



    @Override
    public String toString() {
        return "Name: " + this.getFirstName() +
                " Surname: " + this.getLastName() +
                " Age: " + this.getAge() +
                " Address: " + this.getAddress() +
                " University: " + this.getUniversity() +
                " Email: " + this.email;
    }
}


