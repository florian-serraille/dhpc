package com.devlabs.dhpc.authenticationservice.security;

import com.devlabs.dhpc.authenticationservice.account.AccountService;
import com.devlabs.dhpc.authenticationservice.security.filter.JwtAuthenticationFilter;
import com.devlabs.dhpc.authenticationservice.security.filter.JwtAuthorizationFilter;
import com.devlabs.dhpc.authenticationservice.security.jwt.JwtService;
import com.devlabs.dhpc.authenticationservice.user.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	

	private final AccountService accountService;
	private final JwtService jwtService;
	
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(username -> {
			
			final AppUser appUser = accountService.findUserByName(username)
			                                      .orElseThrow(() -> new UsernameNotFoundException(
					                                      username + " not found"));
			
			return appUser.toUser();
		});
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new JwtAuthenticationFilter(this.authenticationManager(), jwtService));
		http.addFilterBefore(new JwtAuthorizationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
		
	}
}
