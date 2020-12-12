package com.devlabs.dhpc.authenticationservice.security;

import com.devlabs.dhpc.authenticationservice.account.AccountService;
import com.devlabs.dhpc.authenticationservice.security.jwt.JwtService;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Profile("dev")
@Order(HIGHEST_PRECEDENCE)
@Configuration
public class DevSecurityConfig extends SecurityConfig {

	public DevSecurityConfig(final JwtService jwtService,
	                         final AccountService accountService) {

		super(accountService, jwtService);
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

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				       .components(new Components()
						                   .addSecuritySchemes("bearer-key",
						                                       new SecurityScheme().type(SecurityScheme.Type.HTTP)
						                                                           .scheme("bearer")
						                                                           .bearerFormat("JWT")));
	}
}
