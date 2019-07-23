package com.example.summerprojecttest.model;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Indexed
@AnalyzerDef(name = "customanalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = StandardFilterFactory.class),
//                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
//                        @Parameter(name = "language", value = "English")
//                }),

                @TokenFilterDef(
                        factory = NGramFilterFactory.class, // Generate prefix tokens
                        params = {
                                @Parameter(name = "minGramSize", value = "1"),
                                @Parameter(name = "maxGramSize", value = "10")
                        }
                )
        })
@AnalyzerDef(name = "customanalyzer_query",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = StandardFilterFactory.class),
//                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
//                        @Parameter(name = "language", value = "English")
//                })// Lowercase all characters
        })
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userName")
    //@NotEmpty
    private String userName;

    @Column(name = "firstName")
    @NotEmpty
    @Field(termVector = TermVector.YES)
    @Analyzer(definition = "customanalyzer")
    private String firstName;

    @Column(name = "lastName")
    @NotEmpty
    @Field(termVector = TermVector.YES)
    @Analyzer(definition = "customanalyzer")
    private String lastName;

    @Column(name = "title")
    @Field(termVector = TermVector.YES)
    @Analyzer(definition = "customanalyzer")
    private String title;

    @Column(name = "age")
    @NotNull
    private Integer age;

    @Column(name = "address")
    @NotEmpty
    @Lob
    private String address;

    @Column(name = "bio")
    @Lob
    @Field(termVector = TermVector.YES)
    @Analyzer(definition = "customanalyzer")
    private String bio;

    @Column(name = "university")
    @NotEmpty
    @Field(termVector = TermVector.YES)
    @Analyzer(definition = "customanalyzer")
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

    @ManyToMany
    @JoinTable(
            name = "candidate_languages",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    private Set<Language> languages = new HashSet<>();

    @Column(name = "photo")
    private String photo;

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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public Double matchPercent(Job job){

        if (job.getSkills() != null){
            if (job.getSkills().size() != 0){
                double totalSkills = job.getSkills().size();
                double matchedSkills = 0;

                for (Skill skill : job.getSkills()){
                    if (SkillsList.contains(this.getSkills(), skill)){
                        matchedSkills++;
                    }
                }

                return matchedSkills/totalSkills;
            }
        }
        return null;
    }

    public boolean isApplied(Job job){
        for (Application application: this.getApplications()){
            if(application.getJob().equals(job)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Candidate){
            if(((Candidate) obj).getId().equals(this.id)){
                return true;
            }
        }
        return false;
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


