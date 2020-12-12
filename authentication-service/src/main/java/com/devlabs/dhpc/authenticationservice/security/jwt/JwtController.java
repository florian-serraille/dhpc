package com.devlabs.dhpc.authenticationservice.security.jwt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@AllArgsConstructor
public class JwtController {
	
	private final JwtService jwtService;
	
	@GetMapping("/refresh-token")
	@Operation(security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity<Map<String, String>> listUsers(@NotBlank @RequestHeader(AUTHORIZATION) final String token) {
		return ResponseEntity.ok(jwtService.refreshToken(token));
	}
	
}
