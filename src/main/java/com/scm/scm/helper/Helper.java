package com.scm.scm.helper;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    @Value("${server.baseUrl}")
    private String baseUrl;

    public static String getEmailOfLoggedInUser(Authentication authentication){

        // AuthenticationPrincipal principal = (AuthenticationPrincipal)authentication.getPrincipal();

        // agar emailId aaur password se login kiya hai toh email kaise nikalenge
        if(authentication instanceof OAuth2AuthenticationToken){

            var aOAuth2AuthenticationToken =  (OAuth2AuthenticationToken)authentication;
            var clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            var oauth2User = (OAuth2User)authentication.getPrincipal();
            String username = "";

            // sign with google then
            if(clientId.equalsIgnoreCase("google")){
                System.out.println("Getting email from google");
                username = oauth2User.getAttribute("email").toString();
            }

            // sign with github
            else if(clientId.equalsIgnoreCase("github")){
                System.out.println("Getting email from github");
                username = oauth2User.getAttribute("email")!=null? oauth2User.getAttribute("email").toString() : oauth2User.getAttribute("login").toString()+"@gmail.com";
            }

            return username;


        }
        else{
            System.out.println("Getting email from local database");
            return authentication.getName();
        }

    }


    public String getLinkForEmailVerification(String emailToken){
        return this.baseUrl + "/auth/verify-email?token=" + emailToken;
    }

    // public static String getLinkForEmailVerification(String emailToken) {
    //     String baseUrl = System.getenv("APP_BASE_URL"); // Environment variable for the base URL
    //     if (baseUrl == null || baseUrl.isEmpty()) {
    //         baseUrl = "http://localhost:8080"; // Fallback for local development
    //     }
    //     return baseUrl + "/auth/verify-email?token=" + emailToken;
    // }
    

}
