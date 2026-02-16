package com.ecom.my_ecom.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.my_ecom.entities.productreviews;
import com.ecom.my_ecom.reviewsdao.Reviewsdao;
import com.ecom.my_ecom.services.product_service;

import jakarta.validation.Valid;




@RestController
@RequestMapping("/api/products/reviews")
public class reviewscontoller {
	
	@Autowired
	private product_service pro_service;
	
	@PostMapping
	public ResponseEntity<?> addReviews(@RequestBody @Valid Reviewsdao reviews) {
		pro_service.addReviews(reviews);
		return ResponseEntity.status(HttpStatus.CREATED).body("Review created successfully");
	}

	@GetMapping("/{productId}")
	public ResponseEntity<List<Map<String, Object>>> getReviewsByProduct(@PathVariable Long productId) {
		List<productreviews> reviews = pro_service.getReviewsByProductId(productId);
		List<Map<String, Object>> result = reviews.stream()
				.map(r -> {
					Map<String, Object> m = new HashMap<>();
					m.put("id", r.getId());
					m.put("ratings", r.getRatings());
					m.put("comments", r.getComments());
					m.put("date", r.getCreatedAt());
					return m;
				})
				.collect(Collectors.toList());
		return ResponseEntity.ok(result);
	}
}
