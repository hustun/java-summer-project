package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.model.Application;
import com.example.summerprojecttest.model.Candidate;
import com.example.summerprojecttest.model.Job;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
public class HRController {

    private ApplicationService applicationService;
    private JavaMailSender javaMailSender;

    public HRController(ApplicationService applicationService, JavaMailSender javaMailSender) {
        this.applicationService = applicationService;
        this.javaMailSender = javaMailSender;
    }

    @RequestMapping("/hr/{id}/{status}")
    public RedirectView handleApplication(@PathVariable String id, @PathVariable String status){
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attributes.getRequest().getSession();
        if (httpSession.getAttribute("isHR") == null){
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/");
            return redirectView;
        }
        Application application = applicationService.findById(Integer.valueOf(id));

        String title, body;
        switch (status){
            case "accept":
                application.setStatus(StatusType.ACCEPTED);
                title = "Application Accepted";
                body = "Congratulations " + application.getApplicant().getFirstName() + ",\n\nYour application to "
                        + application.getJob().getTitle() + " position is accepted. You can expect someone to get in " +
                        "contact with you soon.\n\n We wish you a good day.";
                break;
            case "reject":
                application.setStatus(StatusType.REJECTED);
                title = "Application Rejected";
                body = "Hello " + application.getApplicant().getFirstName() + ",\n\nWe are sorry to inform you that" +
                        " your application to " + application.getJob().getTitle() + " position is rejected." +
                        " However, you can still continue your search by applying other jobs.\n\n We wish you a good day.";
                break;
            case "inProcess":
                application.setStatus(StatusType.IN_PROCESS);
                title = "Application In Process";
                body = "Hello " + application.getApplicant().getFirstName() + ",\n\nYour application to "
                        + application.getJob().getTitle() + " position is in evaluation. You can expect a result soon." +
                        "\n\n We wish you a good day.";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }

        applicationService.save(application);

        sendEmail(application, title, body);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/jobs/show/" + application.getJob().getId());

        return redirectView;
    }

    @RequestMapping("/loginProcess")
    public RedirectView handleHRLogin(){
        RedirectView redirectView = new RedirectView();

        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attributes.getRequest().getSession();

        if (httpSession.getAttribute("isCandidate") != null && (boolean) httpSession.getAttribute("isCandidate")){
            httpSession.removeAttribute("isCandidate");
        }
        httpSession.setAttribute("isHR", true);
        redirectView.setUrl("/");

        return redirectView;
    }

    private void sendEmail(Application application, String title, String body){
        Candidate candidate = application.getApplicant();
        Job job = application.getJob();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(candidate.getEmail());

        message.setSubject(title);
        message.setText(body);

        javaMailSender.send(message);
    }

}
