package com.innovation.launchpad.authentication.api.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovation.launchpad.authentication.api.model.JwtRequest;
import com.innovation.launchpad.authentication.api.model.JwtResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {	
	private static final long JWT_TOKEN_VALIDITY = 5*60*60;
	private AuthenticationManager authenticationManager;
	private String secret;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String secret) {
        this.authenticationManager = authenticationManager;
        this.secret = secret;
        setFilterProcessesUrl("/authenticate"); 
    }
	
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
        	JwtRequest creds = new ObjectMapper()
                    .readValue(req.getInputStream(), JwtRequest.class);
        	
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = JWT.create()
				.withSubject(((User) auth.getPrincipal()).getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000))
				.sign(Algorithm.HMAC512(secret.getBytes()));
        new ObjectMapper().writer()
        		.writeValue(res.getWriter(), new JwtResponse(token));
        res.getWriter().flush();
    }
    
    protected void unsuccessfulAuthentication​(HttpServletRequest request, 
    		HttpServletResponse response, AuthenticationException failed) 
    		throws IOException, ServletException {
    	super.unsuccessfulAuthentication(request, response, failed);
    	System.out.println("Unsuccessful authentication");
    }
}
