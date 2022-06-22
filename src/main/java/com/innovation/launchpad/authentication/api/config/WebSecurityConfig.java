package com.innovation.launchpad.authentication.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.innovation.launchpad.authentication.api.service.JwtUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("default-user")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${jwt.secret}")
	private String secret;
	@Value("${credentials.default.username}")
	private String[] defaultUser;
	@Value("${credentials.default.password}")
	private String defaultPassword;
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		PasswordEncoder encoder = passwordEncoder();
		return new JwtUserDetailsService(defaultUser, encoder.encode(defaultPassword));
	}
	
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
//				.antMatchers(HttpMethod.POST, "/authenticate").permitAll()
//				.antMatchers(HttpMethod.GET, "/hello").permitAll()
//				.antMatchers(HttpMethod.GET, "/swagger-ui/*").permitAll()
//				.anyRequest().authenticated()
				.anyRequest().permitAll()
				.and()
				.addFilter(new JwtAuthenticationFilter(authenticationManager(), this.secret))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService(), this.secret))
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
