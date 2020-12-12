package com.devlabs.dhpc.authenticationservice.security.jwt;

import com.devlabs.dhpc.authenticationservice.user.AppUser;

import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AppJWT {
	
	private static final String PREFIX = "Bearer ";
	private static final String ACCESS_TOKEN = "access-token";
	private static final String REFRESH_TOKEN = "refresh-token";
	
	private final Map<String, AppToken> jwt;
	
	private AppJWT() {
		
		this.jwt = new HashMap<>();
	}
	
	public static AppJWT build(final AppUser user, final Long accessExpiration,
	                           final Long refreshExpiration, final String issuer) {
		
		final AppJWT appJWT = new AppJWT();
		
		appJWT.jwt.put(ACCESS_TOKEN,
		               AppToken.buildAppToken(user, new Date(System.currentTimeMillis() + accessExpiration), issuer));
		appJWT.jwt.put(REFRESH_TOKEN,
		               AppToken.buildAppToken(user, new Date(System.currentTimeMillis() + refreshExpiration), issuer));
		
		return appJWT;
	}
	
	private static boolean needRoles(String tokenType) {
		return ACCESS_TOKEN.equals(tokenType);
	}
	
	public static AppJWT refreshToken(final AppToken refreshToken, final Long accessDuration) {
		
		final AppUser user = new AppUser(null, refreshToken.getSubject(), null, refreshToken.getAppRoles());
		
		return AppJWT.build(user, accessDuration,
		                    refreshToken.getExpirationAt().toInstant().toEpochMilli() - System.currentTimeMillis(),
		                    refreshToken.getIssuer());
	}
	
	public Map<String, String> sign(final String secret) {
		
		return jwt.entrySet().stream()
		          .map(entry -> new SimpleEntry<>(entry.getKey(),
		                                          entry.getValue().createToken(secret, needRoles(entry.getKey()))))
		          .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
	}
	
}
