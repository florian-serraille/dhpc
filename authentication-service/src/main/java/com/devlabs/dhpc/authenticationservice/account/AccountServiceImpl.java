package com.devlabs.dhpc.authenticationservice.account;

import com.devlabs.dhpc.authenticationservice.role.AppRole;
import com.devlabs.dhpc.authenticationservice.role.AppRoleRepository;
import com.devlabs.dhpc.authenticationservice.user.AppUser;
import com.devlabs.dhpc.authenticationservice.user.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class AccountServiceImpl implements AccountService {
	
	private final AppUserRepository userRepository;
	private final AppRoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public AppUser addNewUser(final AppUser appUser) {
		
		appUser.encryptPassword(passwordEncoder);
		return userRepository.save(appUser);
	}
	
	@Override
	public AppRole addNewRole(final AppRole appRole) {
		return roleRepository.save(appRole);
	}
	
	@Override
	public void addRoleToUser(final String username, final String roleName) {
		
		final AppUser user = userRepository.findByUsername(username).orElseThrow(ResourceNorFoundException::new);
		final AppRole role = roleRepository.findByRoleName(roleName).orElseThrow(ResourceNorFoundException::new);
		
		user.getAppRoles().add(role);
	}
	
	@Override
	public Optional<AppUser> findUserByName(final String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public List<AppUser> findAllUsers() {
		return userRepository.findAll();
	}
}
