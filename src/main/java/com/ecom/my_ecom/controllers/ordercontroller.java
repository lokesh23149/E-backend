package com.ecom.my_ecom.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.my_ecom.entities.orders;
import com.ecom.my_ecom.entities.myentity;
import com.ecom.my_ecom.productrepository.Repository;
import com.ecom.my_ecom.reviewsdao.ordercreateddto;
import com.ecom.my_ecom.reviewsdao.requestorder;
import com.ecom.my_ecom.services.orderservices;

@RestController
@RequestMapping("/api/order")
public class ordercontroller {

	@Autowired
	private orderservices orderservice;

	@Autowired
	private Repository userRepo;

	@PostMapping
	public ResponseEntity<?> createorder(@RequestBody requestorder requestorder) {
		ordercreateddto ordercreted = orderservice.createorder(requestorder);
		return ResponseEntity.ok().body(ordercreted);
	}

	@GetMapping("/admin/orders")
	public ResponseEntity<?> getAdminOrders() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
		}
		String username = ((UserDetails) auth.getPrincipal()).getUsername();
		myentity user = userRepo.findByusername(username).orElse(null);
		if (user == null || !"ROLE_ADMIN".equals(user.getRole())) {
			return ResponseEntity.status(403).body(Map.of("error", "Admin access required"));
		}
		return ResponseEntity.ok(orderservice.getAllOrders());
	}

	@GetMapping("/user/orders")
	public ResponseEntity<?> getUserOrders() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			return ResponseEntity.ok(List.of());
		}
		String username = ((UserDetails) auth.getPrincipal()).getUsername();
		myentity user = userRepo.findByusername(username)
				.orElse(null);
		if (user == null) {
			return ResponseEntity.ok(List.of());
		}
		List<orders> userOrders = orderservice.getOrdersByUserId(user.getId());
		return ResponseEntity.ok(userOrders);
	}

	@GetMapping("/{referenceID}/tracking")
	public ResponseEntity<?> getOrderTracking(@PathVariable String referenceID) {
		orders order = orderservice.getorder(referenceID);
		Map<String, Object> tracking = new HashMap<>();
		tracking.put("orderId", order.getreferenceID());
		tracking.put("referenceId", order.getreferenceID());
		tracking.put("status", order.getStatus() != null ? order.getStatus() : "pending");
		tracking.put("createdAt", order.getCreatedAt());
		tracking.put("trackingNumber", (order.getStatus() != null && ("shipped".equalsIgnoreCase(order.getStatus()) || "delivered".equalsIgnoreCase(order.getStatus())))
				? "TRK-" + order.getreferenceID().substring(0, Math.min(8, order.getreferenceID().length())).toUpperCase()
				: null);
		tracking.put("total", order.getTotal());
		return ResponseEntity.ok().body(tracking);
	}

	@GetMapping("/{referenceID}")
	public ResponseEntity<?> getorder(@PathVariable String referenceID) {
		orders order = orderservice.getorder(referenceID);
		return ResponseEntity.ok().body(order);
	}
}
