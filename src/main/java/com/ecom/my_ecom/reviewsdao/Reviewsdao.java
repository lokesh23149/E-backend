package com.ecom.my_ecom.reviewsdao;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Reviewsdao {

	@NotNull(message = "Product ID is required")
	@JsonProperty("productId")
	private Long productid;
	@NotNull(message = "ratings is required")
	private Double ratings;
	@NotBlank(message = "comment is required")
	private String comments;
	public Long getId() {
		return productid;
	}
	public void setId(Long productid) {
		this.productid = productid;
	}
	public Double getRatings() {
		return ratings;
	}
	public void setRatings(Double ratings) {
		this.ratings = ratings;
	}
	public Long getProductid() {
		return productid;
	}
	public void setProductid(Long productid) {
		this.productid = productid;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Reviewsdao() {
		super();
		
	}
	Reviewsdao(Long productid, Double ratings, String comments) {
		super();
		this.productid = productid;
		this.ratings = ratings;
		this.comments = comments;
	}
	

}
