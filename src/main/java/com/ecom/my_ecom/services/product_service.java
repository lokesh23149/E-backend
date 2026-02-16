package com.ecom.my_ecom.services;

import static org.springframework.data.jpa.domain.Specification.where;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;

import com.ecom.my_ecom.entities.productreviews;
import com.ecom.my_ecom.entities.products;
import com.ecom.my_ecom.productrepository.productrepo;
import com.ecom.my_ecom.productrepository.review_repository;
import com.ecom.my_ecom.reviewsdao.Reviewsdao;
import com.ecom.my_ecom.reviewsdao.imagesdao;
import com.ecom.my_ecom.reviewsdao.productdao;
import com.ecom.my_ecom.specifications.productsepc;

//this is user for services like function how to response with users
@Service
public class product_service {

	@Autowired
	private productrepo repo;

	@Autowired
	private review_repository review_repo;

	public Map<String, Object> productservices(int page, int size) {

		PageRequest pages = PageRequest.of(page, size);

		Page<products> datapage = repo.findAll(pages);
		List<productdao> productdto = datapage.stream().map(this::conterTodao).collect(Collectors.toList());

		Map<String, Object> maping = new HashMap<String, Object>();
		maping.put("products", productdto);
		maping.put("totalElements", datapage.getTotalElements());
		maping.put("totalPages", datapage.getTotalPages());
		maping.put("currentPage", page);
		maping.put("size", size);

		return maping;

	}

	public productdao conterTodao(products products) {
		productdao productdao = new productdao();
		productdao.setId(products.getId());
		productdao.setCategory(products.getCategory());
		productdao.setDiscripsion(products.getDiscripsion());
		productdao.setNumofreviews(products.getNumofreviews());
		productdao.setRatings(products.getRatings() != null ? products.getRatings() : 0.0);
		productdao.setPrice(products.getPrice());
		productdao.setSeller(products.getSeller());
		productdao.setStock(products.getStock());
		productdao.setName(products.getName());
		productdao.setDiscount(products.getDiscount());
		List<Reviewsdao> myreviews= products.getReviews()
				.stream()
				.map(review->{
			Reviewsdao myreview =new Reviewsdao();
			
			myreview.setProductid(review.getId());
			myreview.setComments(review.getComments());
			myreview.setRatings(review.getRatings());
			return myreview;
			
		}).collect(Collectors.toList());
		productdao.setReviews(myreviews);
		
		List<imagesdao> myimages=products.getImages()
				.stream().map(img->{imagesdao myImagesdao=new imagesdao();
				
				myImagesdao.setUrl(img.getPublicid());
				return myImagesdao;
				
				
				}).collect(Collectors.toList());
		productdao.setImages(myimages);
		
		return productdao;
	}

	public products getproductbyid(Long id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("id is not founded :" + id));
	}

	public productdao getProductByIdAsDto(Long id) {
		products product = repo.findById(id).orElseThrow(() -> new RuntimeException("id is not founded :" + id));
		return conterTodao(product);
	}

	@SuppressWarnings("removal")
	public List<products> searchproduct(String category, Double minPrice, Double maxPrice, String keywords,
			Double rating) {
		Specification<products> spec = where(productsepc.findcategory(category))
				.and(productsepc.findbyprice(minPrice, maxPrice)).and(productsepc.findbynameordiscripsion(keywords))
				.and(productsepc.findratings(rating));

		return repo.findAll(spec);

	}

	public void addReviews(Reviewsdao reviews) {

		Long productId = reviews.getProductid() != null ? reviews.getProductid() : reviews.getId();
		products addproduct = repo.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found: " + productId));
		productreviews product_reviews = new productreviews();
		product_reviews.setComments(reviews.getComments());
		product_reviews.setRatings(reviews.getRatings());
		product_reviews.setProductreviews(addproduct);
		product_reviews.setCreatedAt(java.time.Instant.now());

		review_repo.save(product_reviews);

		// Update product rating and review count
		var allReviews = review_repo.findByProductIdOrderByIdDesc(productId);
		double avgRating = allReviews.stream()
				.mapToDouble(r -> r.getRatings() != null ? r.getRatings() : 0)
				.average()
				.orElse(0);
		addproduct.setNumofreviews(allReviews.size());
		addproduct.setRatings(Math.round(avgRating * 10.0) / 10.0);
		repo.save(addproduct);
	}

	public List<productreviews> getReviewsByProductId(Long productId) {
		return review_repo.findByProductIdOrderByIdDesc(productId);
	}

	public List<String> getCategories() {
		return repo.findDistinctCategories();
	}

	public Map<String, Object> searchProductsWithPagination(String category, Double minPrice, Double maxPrice,
			String keywords, Double rating, int page, int size, String sortBy) {
		Specification<products> spec = where(productsepc.findcategory(category))
				.and(productsepc.findbyprice(minPrice, maxPrice))
				.and(productsepc.findbynameordiscripsion(keywords))
				.and(productsepc.findratings(rating));

		org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.unsorted();
		if (sortBy != null && !sortBy.isEmpty()) {
			boolean ascending = !sortBy.startsWith("-");
			String field = sortBy.startsWith("-") ? sortBy.substring(1) : sortBy;
			if ("name".equals(field)) {
				sort = org.springframework.data.domain.Sort.by(ascending ? org.springframework.data.domain.Sort.Direction.ASC : org.springframework.data.domain.Sort.Direction.DESC, "name");
			} else if ("price".equals(field)) {
				sort = org.springframework.data.domain.Sort.by(ascending ? org.springframework.data.domain.Sort.Direction.ASC : org.springframework.data.domain.Sort.Direction.DESC, "price");
			} else if ("rating".equals(field) || "ratings".equals(field)) {
				sort = org.springframework.data.domain.Sort.by(ascending ? org.springframework.data.domain.Sort.Direction.ASC : org.springframework.data.domain.Sort.Direction.DESC, "ratings");
			}
		}

		PageRequest pages = PageRequest.of(page, size, sort);
		Page<products> datapage = repo.findAll(spec, pages);
		List<productdao> productdto = datapage.stream().map(this::conterTodao).collect(Collectors.toList());

		Map<String, Object> maping = new HashMap<String, Object>();
		maping.put("products", productdto);
		maping.put("totalElements", datapage.getTotalElements());
		maping.put("totalPages", datapage.getTotalPages());
		maping.put("currentPage", page);
		maping.put("size", size);

		return maping;
	}
	
	
	
	
	
}
