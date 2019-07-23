package com.example.summerprojecttest.model;

import com.example.summerprojecttest.converter.LocalDateAttributeConverter;
import com.example.summerprojecttest.converter.LocalDateTimeAttributeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Indexed
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    @NotEmpty
    @Field(termVector = TermVector.YES)
    @Analyzer(definition = "customanalyzer")
    private String title;

    @Column(name = "description")
    @NotEmpty
    @Lob
    @Field
    @Analyzer(definition = "customanalyzer")
    private String description;

    /*private Set<Skill> requiredSkills = new HashSet<>();*/

    @Column(name = "activationTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activationTime;
    //@Convert(converter = LocalDateAttributeConverter.class)

    @Column(name = "closingTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closingTime;

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
    @Field
    @Analyzer(definition = "customanalyzer")
    private String companyName;

    @Column(name = "location")
    @NotEmpty
    @Field
    @Analyzer(definition = "customanalyzer")
    private String location;

    @ManyToMany
    @JoinTable(
            name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();

    private long duration;
    private int durationType = -1;

    public Job() {
    }

    public Job(@NotEmpty String title, @NotEmpty String description, @NotNull LocalDateTime activationTime,
               @NotNull LocalDateTime closingTime, @NotNull Status status,
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

    public LocalDateTime getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(LocalDateTime activationTime) {
        this.activationTime = activationTime;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalDateTime closingTime) {
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

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getDurationType() {
        return durationType;
    }

    public void setDurationType(int durationType) {
        this.durationType = durationType;
    }

    public void updateDuration() {
        Long duration = Math.abs(Duration.between(LocalDateTime.now(), this.getActivationTime()).toMinutes());
        this.setDurationType(0);
        if (duration > 60){
            duration = duration / 60;
            this.setDurationType(1);
        }
        if (duration > 24){
            duration = duration /24;
            this.setDurationType(2);
        }
        this.setDuration(duration);
    }

    @Override
    public String toString() {
        return "Title: " + this.getTitle() +
                " Description: " + this.getDescription() +
                " Status: " + this.getStatus() +
                " Company Name: " + this.getCompanyName() +
                " Location: " + this.getLocation();
    }
}
