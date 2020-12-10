package com.devlabs.dhpc.authenticationservice.role;

import com.devlabs.dhpc.authenticationservice.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
	
	Optional<AppRole> findByRoleName(String roleName);
}
