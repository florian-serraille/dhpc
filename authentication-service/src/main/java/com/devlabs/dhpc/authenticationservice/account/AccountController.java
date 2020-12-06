package com.devlabs.dhpc.authenticationservice.account;

import com.devlabs.dhpc.authenticationservice.user.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccountController {
	
	private final AccountService accountService;
	
	@GetMapping("/users")
	public ResponseEntity<List<AppUser>> listUsers() {
		return ResponseEntity.ok(accountService.findAllUsers());
	}
	
	@PostMapping("/users")
	public ResponseEntity<AppUser> addUser(@RequestBody final AppUser user) {
		return ResponseEntity.ok(accountService.addNewUser(user));
	}
	
	@PostMapping("/users/{username}/role")
	public void addRole(@PathVariable final String username, @RequestBody final String roleName) {
		
		accountService.addRoleToUser(username, roleName);
		ResponseEntity.accepted();
	}
}
