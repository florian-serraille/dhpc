package com.devlabs.dhpc.authenticationservice.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.devlabs.dhpc.authenticationservice.account.AccountService;
import com.devlabs.dhpc.authenticationservice.account.ResourceNorFoundException;
import com.devlabs.dhpc.authenticationservice.role.AppRole;
import com.devlabs.dhpc.authenticationservice.user.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {
	
	private final String secret;
	private final Long accessDuration;
	private final Long refreshDuration;
	private final AccountService accountService;
	
	public JwtService(@Value("${token.secret}") final String secret,
	                  @Value("${token.duration.access}") final Long accessDuration,
	                  @Value("${token.duration.refresh}") final Long refreshDuration,
	                  final AccountService accountService) {
		
		this.secret = secret;
		this.accessDuration = accessDuration;
		this.refreshDuration = refreshDuration;
		this.accountService = accountService;
	}
	
	public Map<String, String> buildJwt(final AppUser user, final String issuer) {
		
		return AppJWT.build(user, accessDuration, refreshDuration, issuer).sign(secret);
	}
	
	public Map<String, String> refreshToken(final String token) {
		
		final AppToken appToken = decodeJwt(AppToken.removeBearerPrefix(token));
		final AppJWT appJWT = AppJWT.refreshToken(appToken, accessDuration);
		
		return appJWT.sign(secret);
	}
	
	public AppToken decodeJwt(final String jwt) {
		
		Algorithm algorithm = Algorithm.HMAC256(secret);
		JWTVerifier jwtVerifier = JWT.require(algorithm).build();
		
		final DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
		final AppUser appUser = accountService.findUserByName(decodedJWT.getSubject())
		                                      .orElseThrow(ResourceNorFoundException::new);
		
		return new AppToken(decodedJWT.getSubject(),
		                    appUser.getAppRoles().stream().map(AppRole::getRoleName).collect(Collectors.toList()),
		                    decodedJWT.getIssuer(), decodedJWT.getExpiresAt(), decodedJWT.getIssuedAt());
		
	}
}
