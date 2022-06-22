package com.innovation.launchpad.authentication.api.controller;


import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController {

	// @Autowired
	// TutorialRepository tutorialRepository;

	@GetMapping("/tutorials")
	public String getAllTutorials(@RequestParam(required = false) String title) {
		return "Greetings tutorials";
	}

	@GetMapping("/tutorials/{id}")
	public String getTutorialById(@PathVariable("id") long id) {
		return "greetings by id";
	}



}
