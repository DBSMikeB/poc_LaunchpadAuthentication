package com.innovation.launchpad.authentication.api.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class JwtUserDetailsService implements UserDetailsService {
	
	private final String adminUser;
	private final String reviewerUser;
	private final String submitterUser;
	private final String normalUser;
	private final String defaultEncodedPassword;
	
	public JwtUserDetailsService(String[] users, String defaultEncodedPassword) {
		this.adminUser = users[0];
		this.reviewerUser = users[1];
		this.submitterUser = users[2];
		this.normalUser = users[3];
		this.defaultEncodedPassword = defaultEncodedPassword;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (adminUser.equals(username)) {
			return new User(adminUser, defaultEncodedPassword,
					new ArrayList<>());
		} else if (reviewerUser.equals(username)){
			return new User(reviewerUser, defaultEncodedPassword,
					new ArrayList<>());
		} else if (submitterUser.equals(username)){
			return new User(submitterUser, defaultEncodedPassword,
					new ArrayList<>());
		} else if (normalUser.equals(username)){
			return new User(normalUser, defaultEncodedPassword,
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}