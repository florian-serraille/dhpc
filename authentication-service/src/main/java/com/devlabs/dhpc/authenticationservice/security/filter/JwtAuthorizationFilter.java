package com.devlabs.dhpc.authenticationservice.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.devlabs.dhpc.authenticationservice.security.jwt.AppToken;
import com.devlabs.dhpc.authenticationservice.security.jwt.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
	
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
	                                final FilterChain filterChain) throws ServletException, IOException {
		
		final String authorizationHeader = request.getHeader(AUTHORIZATION);
		
		if (authorizationHeader != null && authorizationHeader.startsWith(AppToken.getPrefix())) {
			
			try {
				
				final String jwt = AppToken.removeBearerPrefix(authorizationHeader);
				final AppToken appToken = jwtService.decodeJwt(jwt);
				final UsernamePasswordAuthenticationToken  authentication = appToken.createAuthentication();
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				filterChain.doFilter(request, response);
				
			} catch (JWTVerificationException ex) {
				
				response.setHeader("error-message", ex.getMessage());
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}
		} else {
			filterChain.doFilter(request, response);
		}
		
	}
}
