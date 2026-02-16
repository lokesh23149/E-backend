package com.ecom.my_ecom.entities;




import java.util.List;

import java.util.stream.Collectors;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
// this is used for creating table as a data in a sql
@Entity

public class products {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@NotBlank(message = "name feild is required")
	private String name;

	@Column(nullable = false)
	@NotNull(message = "price feild is required")
	private Double price;

	private String discripsion;
	
	private String category;
	
	private Double ratings = 0.0;
	
	@NotBlank(message = "seller feild is required")
	private String seller;
	
	@NotNull(message = "stock feild is required")
	private Integer stock;
	
	private Integer numofreviews = 0;
	
	private Integer discount = 0;
	
	public Integer getDiscount() {
		return discount != null ? discount : 0;
	}
	
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	
     public products(Long id, String name, Double price, String discripsion,String category, Double ratings, String seller,Integer stock,List<String> images) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.discripsion = discripsion;
		this.category=category;
		this.ratings = ratings;
		this.seller = seller;
		this.stock = stock;
		this.images=images.stream().map(url->new productimages(url,this)).collect(Collectors.toList());
		
	}
	
	public List<productimages> getImages() {
		return images;
	}

	 public void setImages(java.util.List<productimages> images) {
		 this.images = images;
	 }

	public products() {}
	public String getDiscripsion() {
		return discripsion;
	}
	public void setDiscripsion(String discripsion) {
		this.discripsion = discripsion;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public java.util.List<productreviews> getReviews() {
		return reviews;
	}
	

	public void setId(Long id) {
		this.id = id;
	}
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private  List<productimages> images;

	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private  java.util.List<productreviews> reviews;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDiscription() {
		return discripsion;
	}

	public void setDiscription(String discription) {
		this.discripsion = discription;
	}

	public Double getRatings() {
		return ratings;
	}

	public void setReviews(java.util.List<productreviews> reviews) {
		this.reviews = reviews;
	}
	public void setRatings(Double ratings) {
		this.ratings = ratings;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getNumofreviews() {
		return numofreviews;
	}

	public void setNumofreviews(Integer numofreviews) {
		this.numofreviews = numofreviews;
	}
	

}
