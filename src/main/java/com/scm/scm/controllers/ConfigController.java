package com.scm.scm.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {
    @Value("${server.baseUrl}")
    private String baseUrl;

    @GetMapping("/api/config")
    public String getBaseUrl() {
        return baseUrl;
    }
}
