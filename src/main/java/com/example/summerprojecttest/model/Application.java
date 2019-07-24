package com.example.summerprojecttest.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date")
    @NotNull
    private LocalDateTime date;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate applicant;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public Application() {
    }

    public Application(@NotNull LocalDateTime date) {
        this.date = date;
        this.status = StatusType.PENDING;
    }

    public Application(@NotNull LocalDateTime date, StatusType status) {
        this.date = date;
        this.status = status;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " Date: " + date +
                " Applicant: " + applicant.getFirstName() +
                " Job: " + job.getTitle();
    }
}
