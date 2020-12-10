package com.devlabs.dhpc.authenticationservice.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.devlabs.dhpc.authenticationservice.user.AppUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	
	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request,
	                                            final HttpServletResponse response) throws
	                                                                                AuthenticationException {
		try {
			
			final String jsonRequestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			final ObjectNode node = new ObjectMapper().readValue(jsonRequestBody, ObjectNode.class);
			
			final String username = Optional.ofNullable(node.get("username")).orElse(new TextNode("")).asText();
			final String password = Optional.ofNullable(node.get("password")).orElse(new TextNode("")).asText();
			
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
		} catch (IOException e) {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("", ""));
		}
	}
	
	@Override
	protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
	                                        final FilterChain chain,
	                                        final Authentication authResult) throws IOException, ServletException {
		
		final User user = (User) authResult.getPrincipal();
		
		final Algorithm algorithm = Algorithm.HMAC256("my-secret");
		
		final String jwtAccessToken = JWT.create()
		                      .withSubject(user.getUsername())
		                      .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
		                      .withIssuer(request.getRequestURL().toString())
		                      .withClaim("roles", user.getAuthorities()
		                                              .stream()
		                                              .map(GrantedAuthority::getAuthority)
		                                              .collect(Collectors.toList()))
		                      .sign(algorithm);
		
		final String jwtRefreshToken = JWT.create()
		                                 .withSubject(user.getUsername())
		                                 .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
		                                 .withIssuer(request.getRequestURL().toString())
		                                 .sign(algorithm);
		
		final Map<String, String> token = new HashMap<>();
		token.put("access-token", jwtAccessToken);
		token.put("refresh-token", jwtRefreshToken);
	
		response.setContentType(MediaType.APPLICATION_JSON);
		
		new ObjectMapper().writeValue(response.getOutputStream(), token);
	}
}
