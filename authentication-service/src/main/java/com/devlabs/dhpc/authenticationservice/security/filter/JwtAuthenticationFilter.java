package com.devlabs.dhpc.authenticationservice.security.filter;

import com.devlabs.dhpc.authenticationservice.security.jwt.JwtService;
import com.devlabs.dhpc.authenticationservice.user.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request,
	                                            final HttpServletResponse response) throws
	                                                                                AuthenticationException {
		try {
			
			final String jsonRequestBody = request.getReader().lines().collect(
					Collectors.joining(System.lineSeparator()));
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
		
		response.setContentType(MediaType.APPLICATION_JSON);
		
		final User user = (User) authResult.getPrincipal();
		final String issuer = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString()).build().getHost();
		final Map<String, String> jwt = jwtService.buildJwt(new AppUser(user), issuer);
		
		new ObjectMapper().writeValue(response.getOutputStream(), jwt);
	}
}
