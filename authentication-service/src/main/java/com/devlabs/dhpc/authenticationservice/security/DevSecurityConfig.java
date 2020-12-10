package com.devlabs.dhpc.authenticationservice.security;

import com.devlabs.dhpc.authenticationservice.account.AccountService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Profile("dev")
@Order(HIGHEST_PRECEDENCE)
@Configuration
public class DevSecurityConfig extends SecurityConfig {
	
	public DevSecurityConfig(final AccountService accountService) {
		super(accountService);
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		
		http.headers().frameOptions().disable(); // h2 console
		
		http.authorizeRequests()
		    .antMatchers("/h2-console/**",
		                 "/swagger-ui/**",
		                 "/swagger-ui.html",
		                 "/swagger-resources/**",
		                 "/v3/api-docs/**")
		    .permitAll();
		
		super.configure(http);
	}
}
