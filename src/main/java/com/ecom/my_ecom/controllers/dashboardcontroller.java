package com.ecom.my_ecom.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.my_ecom.entities.myentity;
import com.ecom.my_ecom.productrepository.Repository;
import com.ecom.my_ecom.services.dashboardservice;

@RestController
@RequestMapping("/api/dashboard")
public class dashboardcontroller {

    @Autowired
    private dashboardservice dashboardService;

    @Autowired
    private Repository userRepo;

    @GetMapping
    public ResponseEntity<?> getDashboardData() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        myentity user = userRepo.findByusername(username).orElse(null);
        if (user == null || !"ROLE_ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).body(Map.of("error", "Admin access required"));
        }

        Map<String, Object> data = dashboardService.getDashboardStats();
        return ResponseEntity.ok(data);
    }
}
