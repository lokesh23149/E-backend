package com.ecom.my_ecom.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.my_ecom.productrepository.Repository;
import com.ecom.my_ecom.services.wishlistservice;

@RestController
@RequestMapping("/api/wishlist")
public class wishlistcontroller {

    @Autowired
    private wishlistservice wishlistService;

    @Autowired
    private Repository userRepo;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return userRepo.findByusername(username).map(u -> u.getId()).orElse(null);
    }

    @GetMapping
    public ResponseEntity<?> getWishlist() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.ok(List.of());
        }
        List<Map<String, Object>> items = wishlistService.getWishlistByUserId(userId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToWishlist(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Please login to add to wishlist"));
        }
        try {
            Map<String, Object> item = wishlistService.addToWishlist(userId, productId);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Please login to remove from wishlist"));
        }
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok(Map.of("message", "Removed from wishlist"));
    }

    @GetMapping("/check/{productId}")
    public ResponseEntity<?> checkInWishlist(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.ok(Map.of("inWishlist", false));
        }
        boolean inWishlist = wishlistService.isInWishlist(userId, productId);
        return ResponseEntity.ok(Map.of("inWishlist", inWishlist));
    }
}
