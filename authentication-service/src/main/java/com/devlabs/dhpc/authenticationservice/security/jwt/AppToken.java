package com.devlabs.dhpc.authenticationservice.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.devlabs.dhpc.authenticationservice.role.AppRole;
import com.devlabs.dhpc.authenticationservice.user.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PACKAGE;

@Getter(value = PACKAGE)
@AllArgsConstructor(access = PACKAGE)
public class AppToken {
	
	static final String PREFIX = "Bearer ";
	static final String ROLE = "roles";
	
	private final String subject;
	private final List<String> roles;
	private final String issuer;
	private final Date expirationAt;
	private final Date issuedAt;
	
	public static AppToken buildAppToken(final AppUser user, final Date expiration, final String issuer) {
		
		return new AppToken(user.getUsername(), extractRoles(user), issuer, expiration, null);
	}
	
	public static String getPrefix() {
		return PREFIX;
	}
	
	List<GrantedAuthority> createAuthorities() {
		return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
	
	public UsernamePasswordAuthenticationToken createAuthentication() {
		
		return new UsernamePasswordAuthenticationToken(this.subject,
		                                               null,
		                                               this.createAuthorities());
	}
	
	public String createToken(final String secret, final boolean withRoles) {
		
		final JWTCreator.Builder builder = JWT.create()
		                                      .withSubject(this.subject)
		                                      .withExpiresAt(this.expirationAt)
		                                      .withIssuer(issuer)
		                                      .withIssuedAt(issuedAt == null ? new Date() : issuedAt);
		
		if (withRoles) {
			builder.withClaim(ROLE, roles);
		}
		
		return builder.sign(getAlgorithm(secret));
	}
	
	static Algorithm getAlgorithm(final String secret) {
		return Algorithm.HMAC256(secret);
	}
	
	private static List<String> extractRoles(final AppUser user) {
		return user.getAppRoles()
		           .stream()
		           .map(AppRole::getRoleName)
		           .collect(Collectors.toList());
	}
	
	public static String removeBearerPrefix(final String signedToken) {
		
		Assert.isTrue(signedToken != null && signedToken.length() > PREFIX.length(),
		              "Unable to remove " + PREFIX + " for token");
		
		return signedToken.substring(PREFIX.length());
	}
	
	public List<AppRole> getAppRoles() {
		
		return this.roles.stream()
		                 .map(role -> new AppRole(null, role))
		                 .collect(Collectors.toList());
	}
}
