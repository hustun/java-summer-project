package com.example.summerprojecttest.model;

import com.example.summerprojecttest.converter.LocalDateAttributeConverter;
import com.example.summerprojecttest.converter.LocalDateTimeAttributeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    @NotEmpty
    private String title;

    @Column(name = "description")
    @NotEmpty
    @Lob
    private String description;

    /*private Set<Skill> requiredSkills = new HashSet<>();*/

    @Column(name = "activationTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate activationTime;
    //@Convert(converter = LocalDateAttributeConverter.class)

    @Column(name = "closingTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closingTime;

    public enum Status{
        ACTIVE,
        INACTIVE
    }

    @Column(name = "jobStatus")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "job")
    private Set<Application> applications = new HashSet<>();

    @Column(name = "companyName")
    @NotEmpty
    private String companyName;

    @Column(name = "location")
    @NotEmpty
    private String location;

    public Job() {
    }

    public Job(@NotEmpty String title, @NotEmpty String description, @NotNull LocalDate activationTime,
               @NotNull LocalDate closingTime, @NotNull Status status,
               @NotEmpty String companyName, @NotEmpty String location) {
        this.title = title;
        this.description = description;
        this.activationTime = activationTime;
        this.closingTime = closingTime;
        this.status = status;
        this.companyName = companyName;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public Set<Skill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Set<Skill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }*/

    public LocalDate getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(LocalDate activationTime) {
        this.activationTime = activationTime;
    }

    public LocalDate getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalDate closingTime) {
        this.closingTime = closingTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
