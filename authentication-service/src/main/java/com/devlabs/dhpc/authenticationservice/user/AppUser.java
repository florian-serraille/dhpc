package com.devlabs.dhpc.authenticationservice.user;

import com.devlabs.dhpc.authenticationservice.role.AppRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String username;
	@JsonProperty(access = WRITE_ONLY)
	private String password;
	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<AppRole> appRoles = new ArrayList<>();
	
	public void encryptPassword(final PasswordEncoder passwordEncoder) {
		this.password = (passwordEncoder.encode(this.password));
	}
	
	public AppUser(final User user) {
		
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.appRoles = user.getAuthorities().stream()
		                    .map(ga -> new AppRole(null, ga.getAuthority()))
		                    .collect(Collectors.toList());
	}
	
	public User toUser() {
		
		return new User(username,
		                password,
		                appRoles.stream()
		                        .map(appRole -> new SimpleGrantedAuthority(appRole.getRoleName()))
		                        .collect(Collectors.toList()));
	}
}
