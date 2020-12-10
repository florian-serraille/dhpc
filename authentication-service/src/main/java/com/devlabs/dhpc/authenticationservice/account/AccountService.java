package com.devlabs.dhpc.authenticationservice.account;

import com.devlabs.dhpc.authenticationservice.role.AppRole;
import com.devlabs.dhpc.authenticationservice.user.AppUser;

import java.util.List;
import java.util.Optional;

public interface AccountService {

	AppUser addNewUser(AppUser appUser);
	AppRole addNewRole(AppRole appRole);
	void addRoleToUser(String username, String roleName);
	Optional<AppUser> findUserByName(String username);
	List<AppUser> findAllUsers();
}
