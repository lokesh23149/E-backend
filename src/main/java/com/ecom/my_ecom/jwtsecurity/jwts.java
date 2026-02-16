package com.ecom.my_ecom.jwtsecurity;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;

@Component
public class jwts {

	private static final String SECRET_KEY_STRING = "8f3a7b2c9d4e1f6a5b8c7d2e9f0a3b6c4d8e2f9a0b5c8d7e1f4a9b2c5d8e3f7a";
	private final SecretKey SECRET_String = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

	public String gendratar(UserDetails userdetail) {
		return Jwts.builder().subject(userdetail.getUsername()).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 120))
				.signWith(SECRET_String, Jwts.SIG.HS256).compact();

	}
	
public	boolean validatetoken(String token,UserDetails userdetail){
	
return	extractusername(token).equals(userdetail.getUsername());
		
	}

	public String extractusername(String token) {
	return	Jwts.parser().verifyWith(SECRET_String).build().parseSignedClaims(token).getPayload().getSubject();

	}

}