package com.innovation.launchpad.authentication.api.controller;

import java.io.IOException;
import java.io.StringWriter;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// import io.swagger.v3.oas.annotations.Operation;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovation.launchpad.authentication.api.model.UserGroupResponse;

@RestController
@CrossOrigin()
public class UserGroupController {


	@GetMapping({ "/userGroup" })
	public String userGroup() throws JsonGenerationException, JsonMappingException, IOException {
		StringWriter response = new StringWriter();
		new ObjectMapper().writer()
			.writeValue(response, new UserGroupResponse("ADMIN"));
		
		return response.toString();
	}

}
