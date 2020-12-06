package com.devlabs.dhpc.authenticationservice.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AppRole {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String roleName;
}
