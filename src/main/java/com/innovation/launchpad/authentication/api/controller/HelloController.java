package com.innovation.launchpad.authentication.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
// @CrossOrigin()
@RequestMapping("/api")
public class HelloController {
    @GetMapping("/hello")
    public String Hello(@RequestParam(required = false) String title) {
        return "hello from Spring Boot! updated from ci/cd again";
    }


}
