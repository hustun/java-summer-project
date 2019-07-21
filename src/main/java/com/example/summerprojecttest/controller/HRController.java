package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.model.Application;
import com.example.summerprojecttest.model.Candidate;
import com.example.summerprojecttest.model.StatusType;
import com.example.summerprojecttest.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HRController {

    private ApplicationService applicationService;
    private JavaMailSender javaMailSender;

    public HRController(ApplicationService applicationService, JavaMailSender javaMailSender) {
        this.applicationService = applicationService;
        this.javaMailSender = javaMailSender;
    }

    @RequestMapping("/hr/{id}/accept")
    public RedirectView handleApplication(@PathVariable String id ){
        Application application = applicationService.findById(Integer.valueOf(id));

        application.setStatus(StatusType.ACCEPTED);

        applicationService.save(application);

        sendEmail(application.getApplicant());

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/jobs/show/" + application.getJob().getId());

        return redirectView;
    }

    private void sendEmail(Candidate candidate){
        String email = candidate.getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("hasbey1997@gmail.com");

        message.setSubject("Testing Project");
        message.setText("Hey");

        javaMailSender.send(message);
    }

}
