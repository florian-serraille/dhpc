package com.devlabs.dhpc.authenticationservice.security;

import com.devlabs.dhpc.authenticationservice.account.AccountService;
import com.devlabs.dhpc.authenticationservice.security.filter.JwtAuthenticationFilter;
import com.devlabs.dhpc.authenticationservice.user.AppUser;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final AccountService accountService;
	
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
				
				final AppUser appUser = accountService.findUserByName(username)
				                                      .orElseThrow(() -> new UsernameNotFoundException(
						                                      username + " not found"));
				
				return new User(appUser.getUsername(),
				                appUser.getPassword(),
				                appUser.getAppRoles().stream()
				                       .map(appRole -> new SimpleGrantedAuthority(appRole.getRoleName()))
				                       .collect(Collectors.toList()));
			}
		});
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new JwtAuthenticationFilter(super.authenticationManager()));
		
	}
}
