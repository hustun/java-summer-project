package com.example.summerprojecttest.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date")
    @NotNull
    private LocalTime date;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate applicant;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public Application() {
    }

    public Application(@NotNull LocalTime date) {
        this.date = date;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getDate() {
        return date;
    }

    public void setDate(LocalTime date) {
        this.date = date;
    }

    public Candidate getApplicant() {
        return applicant;
    }

    public void setApplicant(Candidate applicant) {
        this.applicant = applicant;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " Date: " + date +
                " Applicant: " + applicant.getFirstName();
    }
}
