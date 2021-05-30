package com.store.user.management.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.store.user.management.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${user.app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${user.app.jwtExpiration}")
	private int jwtExpiration;
	
	public String generateJwtToken(Authentication auth) {
		
		UserDetailsImpl userPrinciple = (UserDetailsImpl) auth.getPrincipal();
		
		return Jwts.builder()
				.setSubject(userPrinciple.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpiration))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
		
	}
	
	public String getUsernameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		}catch(SignatureException e) {
			logger.error("Invalid JWT signature: {}",e.getMessage());
		}catch(MalformedJwtException e) {
			logger.error("Invalid JWT token: {}",e.getMessage());
		}catch(ExpiredJwtException e) {
			logger.error("Invalid JWT expired: {}",e.getMessage());
		}catch(UnsupportedJwtException e) {
			logger.error("Invalid JWT unsupported: {}",e.getMessage());
		}catch(IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}",e.getMessage());
		}
		return false;
	}
}
