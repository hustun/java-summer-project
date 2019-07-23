package com.example.summerprojecttest.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class BlacklistEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "id")
    private Candidate candidate;

    @Column(name = "reason")
    @NotEmpty
    private String reason;

    public BlacklistEntry() {
    }

    public BlacklistEntry(Candidate candidate, String reason) {
        this.candidate = candidate;
        this.reason = reason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
