package com.devlabs.dhpc.authenticationservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Profile("dev")
@Configuration
@EnableWebSecurity
public class DevSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	public void configure(final WebSecurity web) throws Exception {
		
		super.configure(web);
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.headers().frameOptions().disable(); // h2 console
		http.authorizeRequests().anyRequest().permitAll();
	}
}
