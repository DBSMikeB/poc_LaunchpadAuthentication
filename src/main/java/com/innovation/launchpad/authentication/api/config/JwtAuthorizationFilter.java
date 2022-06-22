package com.innovation.launchpad.authentication.api.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private UserDetailsService jwtUserDetailsService;
	private String secret;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsService jwtUserDetailsService,
			String secret) {
		super(authenticationManager);
		this.jwtUserDetailsService = jwtUserDetailsService;
		this.secret = secret;
	}
	
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;
		
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} 
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		//Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
			// if token is valid configure Spring Security to manually set authentication
			if (validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, new ArrayList<>());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

	private String getUsernameFromToken(String token) {
		return JWT.decode(token).getSubject();
	}
	
	private Boolean validateToken(String token, UserDetails userDetails) {
		final String username = JWT.require(Algorithm.HMAC512(secret.getBytes()))
				.build()
				.verify(token)
				.getSubject();
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = JWT.decode(token).getExpiresAt();
		return expiration.before(new Date());
	}
}
