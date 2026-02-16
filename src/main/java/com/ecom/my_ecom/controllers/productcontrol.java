package com.ecom.my_ecom.controllers;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.my_ecom.entities.products;
import com.ecom.my_ecom.reviewsdao.productdao;
import com.ecom.my_ecom.services.product_service;




@RestController
@RequestMapping("/api/products")
public class productcontrol {
	
	@Autowired
	private product_service pro_service;

	@GetMapping("/categories")
	public List<String> getCategories() {
		return pro_service.getCategories();
	}

	@GetMapping
	public Map<String,Object> getallproduct(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int size,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) String keywords,
			@RequestParam(required = false) Double rating,
			@RequestParam(required = false, defaultValue = "name") String sortBy) {

		boolean hasFilters = (category != null && !category.isEmpty())
				|| minPrice != null
				|| maxPrice != null
				|| (keywords != null && !keywords.trim().isEmpty())
				|| (rating != null && rating > 0);

		if (hasFilters) {
			return pro_service.searchProductsWithPagination(category, minPrice, maxPrice, keywords, rating, page, size, sortBy);
		}
		return pro_service.productservices(page, size);
	}

	@GetMapping("/{id}")
	public productdao getproductbyid(@PathVariable Long id) {
		return pro_service.getProductByIdAsDto(id);
	}

	@GetMapping("/search")
	public List<products> getproductbysearch(
			@RequestParam(required = false) String category,
			@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) String keywords,
			@RequestParam(required = false) Double ratings) {
		return pro_service.searchproduct(category, minPrice, maxPrice, keywords, ratings);
	}

}
