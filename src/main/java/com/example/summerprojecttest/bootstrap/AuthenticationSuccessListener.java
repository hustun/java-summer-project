package com.example.summerprojecttest.bootstrap;

import com.example.summerprojecttest.services.CandidateServiceImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationSuccessListener  implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    CandidateServiceImpl candidateService;

    public AuthenticationSuccessListener(CandidateServiceImpl candidateService) {
        this.candidateService = candidateService;
    }

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        System.out.println("Successful login.");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        System.out.println(candidateService.findByUserName(name).getId());

        try {
            URL url = new URL("https://www.linkedin.com/oauth/v2/authorization");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            //con.setRequestProperty("Content-Type", "application/json");

            int status = con.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            System.out.println(content);

            con.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
