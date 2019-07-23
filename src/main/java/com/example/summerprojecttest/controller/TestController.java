package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.model.Blacklist;
import com.example.summerprojecttest.model.Candidate;
import com.example.summerprojecttest.repo.ApplicationRepository;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TestController {

    private ApplicationRepository applicationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TestController(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @RequestMapping("/test")
    public String showTestPage(Model model){
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attributes.getRequest().getSession();
        System.out.println("Is Candidate: " + httpSession.getAttribute("isCandidate"));
        System.out.println("Is HR: " + httpSession.getAttribute("isHR"));


        entityManager=entityManager.getEntityManagerFactory().createEntityManager();
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Candidate.class)
                .overridesForField("firstName", "customanalyzer_query")
                .overridesForField("lastName", "customanalyzer_query")
                .overridesForField("bio", "customanalyzer_query")
                .overridesForField("university", "customanalyzer_query")
                .get();

        org.apache.lucene.search.Query query = queryBuilder
                .simpleQueryString()
                .onFields("firstName", "lastName", "bio", "university")
                .matching("Has")
                .createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(query, Candidate.class);

        System.out.println("Results: " + jpaQuery.getResultSize());
        List<Candidate> results = jpaQuery.getResultList();

        for (Candidate candidate : results){
            System.out.println(candidate.toString());
        }

        model.addAttribute("applications", applicationRepository.findAll());
        return "test";
    }
}
