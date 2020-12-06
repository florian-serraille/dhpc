package com.devlabs.dhpc.authenticationservice.account;

import com.devlabs.dhpc.authenticationservice.role.AppRole;
import com.devlabs.dhpc.authenticationservice.user.AppUser;

import java.util.List;

public interface AccountService {

	AppUser addNewUser(AppUser appUser);
	AppRole addNewRole(AppRole appRole);
	void addRoleToUser(String username, String roleName);
	AppUser findUserByName(String username);
	List<AppUser> findAllUsers();
}
