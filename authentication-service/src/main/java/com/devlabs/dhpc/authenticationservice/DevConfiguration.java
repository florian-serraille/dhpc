package com.devlabs.dhpc.authenticationservice;

import com.devlabs.dhpc.authenticationservice.account.AccountService;
import com.devlabs.dhpc.authenticationservice.role.AppRole;
import com.devlabs.dhpc.authenticationservice.user.AppUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@Profile("dev")
@Configuration
public class DevConfiguration {
	
	@Bean
	CommandLineRunner start(final AccountService accountService) {
		
		return args -> {
			
			accountService.addNewRole(new AppRole(null, "USER"));
			accountService.addNewRole(new AppRole(null, "ADMIN"));
			accountService.addNewRole(new AppRole(null, "CUSTOMERS_MANAGER"));
			accountService.addNewRole(new AppRole(null, "PRODUCTS_MANAGER"));
			accountService.addNewRole(new AppRole(null, "BILLS_MANAGER"));
			
			accountService.addNewUser(new AppUser(null, "user1", "password1", Collections.emptyList()));
			accountService.addNewUser(new AppUser(null, "user2", "password2", Collections.emptyList()));
			accountService.addNewUser(new AppUser(null, "user3", "password3", Collections.emptyList()));
			accountService.addNewUser(new AppUser(null, "user4", "password4", Collections.emptyList()));
			accountService.addNewUser(new AppUser(null, "admin", "password", Collections.emptyList()));
			
			accountService.addRoleToUser("user1", "USER");
			accountService.addRoleToUser("user2", "USER");
			accountService.addRoleToUser("user2", "CUSTOMERS_MANAGER");
			accountService.addRoleToUser("user3", "USER");
			accountService.addRoleToUser("user3", "PRODUCTS_MANAGER");
			accountService.addRoleToUser("user4", "USER");
			accountService.addRoleToUser("user4", "BILLS_MANAGER");
			accountService.addRoleToUser("admin", "ADMIN");
		};
	}
}
