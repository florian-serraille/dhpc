package com.devlabs.dhpc.authenticationservice.account;

import com.devlabs.dhpc.authenticationservice.user.AppUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccountController {
	
	private final AccountService accountService;
	
	@GetMapping("/users")
	@PreAuthorize("hasAnyAuthority('USER')")
	@Operation(security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity<List<AppUser>> listUsers() {
		return ResponseEntity.ok(accountService.findAllUsers());
	}
	
	
	@PostMapping("/users")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@Operation(security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity<AppUser> addUser(@RequestBody final AppUser user) {
		return ResponseEntity.ok(accountService.addNewUser(user));
	}
	
	@PostMapping("/users/{username}/role")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@Operation(security = { @SecurityRequirement(name = "bearer-key") })
	public void addRole(@PathVariable final String username, @RequestBody final String roleName) {
		
		accountService.addRoleToUser(username, roleName);
		ResponseEntity.accepted();
	}
}
