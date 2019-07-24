package com.example.summerprojecttest.controller;

import com.example.summerprojecttest.model.Candidate;
import com.example.summerprojecttest.services.CandidateServiceImpl;
import com.unboundid.util.json.JSONArray;
import com.unboundid.util.json.JSONException;
import com.unboundid.util.json.JSONObject;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class LinkedInController {

    private final String clientId="78mhye2e5ha1od";
    private final String clientSecret="GrmCYYWLuC4HOjEO";
    private final String redirectUrl="http://localhost:8080/linkedin";

    private CandidateServiceImpl candidateService;

    public LinkedInController(CandidateServiceImpl candidateService) {
        this.candidateService = candidateService;
    }

    /* @GetMapping("/")
    public String index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        System.out.println(userId);
        return "Welcome " + userId;

    }*/


    //get client id and client Secret by creating a app in
    //https://www.linkedin.com developers
    //and set your redirect url

    //create button on your page and hit this get request
    @GetMapping("/authorization")
    public RedirectView authorization() {
        RedirectView redirectView = new RedirectView();
        String authorizationUri="https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id="+clientId+"&redirect_uri="+redirectUrl+"&state=leellee&scope=r_liteprofile%20r_emailaddress";
        redirectView.setUrl(authorizationUri);
        return redirectView;
    }

    //after login in your linkedin account your app will hit this get request
    @GetMapping("/linkedin")

    //now store your authorization code
    public RedirectView home(@RequestParam("code") String authorizationCode, Model model) throws JSONException {
        RedirectView redirectView = new RedirectView();

        //to trade your authorization code for access token
        String accessTokenUri ="https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code&code="+authorizationCode+"&redirect_uri="+redirectUrl+"&client_id="+clientId+"&client_secret="+clientSecret+"";
        // linkedin api to get linkedidn profile detail
        String linedkinDetailUri = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";
        String linedkinProfileUri = "https://api.linkedin.com/v2/me";
        String linkedinPhotoUri = "https://api.linkedin.com/v2/me?projection=(id,profilePicture(displayImage~:playableStreams))";

        //store your access token
        RestTemplate restTemplate = new RestTemplate();
        String accessTokenRequest = restTemplate.getForObject(accessTokenUri, String.class);
        JSONObject jsonObjOfAccessToken = new JSONObject(accessTokenRequest);
        System.out.println(jsonObjOfAccessToken);
        String accessToken = jsonObjOfAccessToken.getField("access_token").toString();
        System.out.println(accessToken);

        //trade your access token
        HttpHeaders headers = new HttpHeaders();
        System.out.println("Authorization, Bearer " +accessToken.substring(1,accessToken.length()-1));
        headers.add("Authorization", "Bearer " +accessToken.substring(1,accessToken.length()-1));
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> linkedinDetailRequest = restTemplate.exchange(linedkinDetailUri, HttpMethod.GET, entity, String.class);
        ResponseEntity<String> linkedinProfileRequest = restTemplate.exchange(linedkinProfileUri, HttpMethod.GET, entity, String.class);
        ResponseEntity<String> linkedinPhotoRequest = restTemplate.exchange(linkedinPhotoUri, HttpMethod.GET, entity, String.class);


        //store json data
        JSONObject jsonObjOfLinkedinDetail = new JSONObject(linkedinDetailRequest.getBody());
        JSONObject jsonObjOfLinkedinProfile = new JSONObject(linkedinProfileRequest.getBody());
        JSONObject jsonObjOfLinkedinPhoto = new JSONObject(linkedinPhotoRequest.getBody());

        //print json data
        System.out.println("-----------JSON-------------");
        System.out.println(jsonObjOfLinkedinDetail);
        System.out.println(jsonObjOfLinkedinProfile);
        System.out.println(jsonObjOfLinkedinPhoto);
        System.out.println("-----------JSON-------------");

        JsonParser jsonParser = JsonParserFactory.getJsonParser();

        //mail matcher
        String email = getProfileInfo(jsonObjOfLinkedinDetail, "emailAddress\":\"[^\"]+", 15, "Email: ");
        //--

        //name matcher
        String firstName = getProfileInfo(jsonObjOfLinkedinProfile, "localizedFirstName\":\"[^\"]+", 21, "First Name: ");
        //--

        //last name matcher
        String lastName = getProfileInfo(jsonObjOfLinkedinProfile, "localizedLastName\":\"[^\"]+", 20, "Last Name: ");
        //--

        //last name matcher
        String photo = getProfileInfo(jsonObjOfLinkedinPhoto, "identifier\":\"[^\"]+", 13, "URL: ");
        //--


        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attributes.getRequest().getSession();

        Authentication auth = new UsernamePasswordAuthenticationToken(email, null
                ,new ArrayList<GrantedAuthority>());

        SecurityContextHolder.getContext().setAuthentication(auth);
        SecurityContext sc = SecurityContextHolder.getContext();
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
        httpSession.setAttribute("firstName", firstName);
        httpSession.setAttribute("lastName", lastName);
        httpSession.setAttribute("photo", photo);
        
        if (httpSession.getAttribute("isHR") != null && (boolean) httpSession.getAttribute("isHR")){
            httpSession.removeAttribute("isHR");
        }
        httpSession.setAttribute("isCandidate", true);


        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

        model.addAttribute("userName", email);

        if( candidateService.findByEmail(email) == null ){
            Candidate candidate = new Candidate();
            System.out.println("No candidate found with this email.");
            redirectView.setUrl("/candidate/newCandidateForm");

            return redirectView;
        }else{
            System.out.println("A candidate found with this email.");
            redirectView.setUrl("/");
        }

        return redirectView;
    }

    private String getProfileInfo(JSONObject jsonObject, String regex, int i, String s2) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(jsonObject.toString());
        boolean found = false;

        if (matcher.find()) {
            System.out.println("I found the text " + matcher.group() + " starting at index " +
                    matcher.start() + " and ending at index " + matcher.end());
            found = true;
        }
        if (!found) {
            System.out.println("No match found.");
        }
        String info = jsonObject.toString().substring(matcher.start() + i, matcher.end());
        System.out.println(s2 + info);
        return info;
    }
}
