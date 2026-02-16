package com.ecom.my_ecom.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.my_ecom.entities.myentity;
import com.ecom.my_ecom.jwtsecurity.jwts;
import com.ecom.my_ecom.productrepository.Repository;


@RestController
@RequestMapping("/auth")
public class jwt_connector {

	@Autowired
	private AuthenticationManager authmanager;

	@Autowired
	private jwts j;

	@Autowired
	private Repository userRepo;

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody myentity user) {
		try {
			if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("error", "Username is required", "message", "Username is required"));
			}
			if (user.getPassword() == null || user.getPassword().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("error", "Password is required", "message", "Password is required"));
			}

			Authentication auth = authmanager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

			UserDetails userdetail = (UserDetails) auth.getPrincipal();
			String token = j.gendratar(userdetail);

			myentity dbUser = userRepo.findByusername(userdetail.getUsername())
					.orElseThrow(() -> new RuntimeException("User not found"));

			Map<String, Object> userData = buildUserResponse(dbUser);

			Map<String, Object> response = new HashMap<>();
			response.put("token", token);
			response.put("user", userData);

			return ResponseEntity.ok(response);
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "Invalid username or password", "message", "Invalid username or password"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", e.getMessage() != null ? e.getMessage() : "Authentication failed",
							"message", e.getMessage() != null ? e.getMessage() : "Authentication failed"));
		}
	}

	@PutMapping("/profile")
	public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, Object> profileData) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("error", "Not authenticated", "message", "Please login to update profile"));
			}

			String username = ((UserDetails) auth.getPrincipal()).getUsername();
			myentity dbUser = userRepo.findByusername(username)
					.orElseThrow(() -> new RuntimeException("User not found"));

			// Update fields from request (support both frontend and backend field names)
			if (profileData.containsKey("name")) {
				dbUser.setName((String) profileData.get("name"));
			}
			if (profileData.containsKey("firstName") && profileData.containsKey("lastName")) {
				String firstName = profileData.get("firstName") != null ? (String) profileData.get("firstName") : "";
				String lastName = profileData.get("lastName") != null ? (String) profileData.get("lastName") : "";
				dbUser.setName((firstName + " " + lastName).trim());
			}
			if (profileData.containsKey("email")) {
				dbUser.setGmail((String) profileData.get("email"));
			}
			if (profileData.containsKey("gmail")) {
				dbUser.setGmail((String) profileData.get("gmail"));
			}
			if (profileData.containsKey("phone")) {
				dbUser.setPhone((String) profileData.get("phone"));
			}
			if (profileData.containsKey("address")) {
				dbUser.setAddress((String) profileData.get("address"));
			}
			if (profileData.containsKey("city")) {
				dbUser.setCity((String) profileData.get("city"));
			}
			if (profileData.containsKey("state")) {
				dbUser.setState((String) profileData.get("state"));
			}
			if (profileData.containsKey("zipCode")) {
				dbUser.setZipCode(profileData.get("zipCode") != null ? String.valueOf(profileData.get("zipCode")) : null);
			}

			myentity savedUser = userRepo.save(dbUser);

			Map<String, Object> userData = buildUserResponse(savedUser);
			return ResponseEntity.ok(Map.of("user", userData));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", e.getMessage() != null ? e.getMessage() : "Failed to update profile"));
		}
	}

	private Map<String, Object> buildUserResponse(myentity dbUser) {
		Map<String, Object> userData = new HashMap<>();
		userData.put("id", dbUser.getId());
		userData.put("username", dbUser.getUsername());
		userData.put("name", dbUser.getName());
		userData.put("email", dbUser.getGmail() != null ? dbUser.getGmail() : dbUser.getUsername());
		userData.put("role", dbUser.getRole());
		userData.put("phone", dbUser.getPhone());
		userData.put("address", dbUser.getAddress());
		userData.put("city", dbUser.getCity());
		userData.put("state", dbUser.getState());
		userData.put("zipCode", dbUser.getZipCode());
		// Parse name into firstName/lastName for frontend form compatibility
		if (dbUser.getName() != null && !dbUser.getName().isEmpty()) {
			String[] parts = dbUser.getName().trim().split("\\s+", 2);
			userData.put("firstName", parts[0] != null ? parts[0] : "");
			userData.put("lastName", parts.length > 1 ? parts[1] : "");
		} else {
			userData.put("firstName", "");
			userData.put("lastName", "");
		}
		return userData;
	}
}
