package com.ecom.my_ecom.reviewsdao;

import java.util.List;





public class productdao {
	private Long id;

	private String name;

	private Double price;

	private String discripsion;

	private String category;

	private Double ratings = 0.0;

	private String seller;

	private Integer stock;

	private Integer numofreviews = 0;
	
	private Integer discount = 0;

	public Integer getDiscount() {
		return discount != null ? discount : 0;
	}
	
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public productdao(Long id, String name, Double price, String discripsion, String category, Double ratings,
			String seller, Integer stock) {

		this.id = id;
		this.name = name;
		this.price = price;
		this.discripsion = discripsion;
		this.category = category;
		this.ratings = ratings;
		this.seller = seller;
		this.stock = stock;

	}

	public String getCategery() {
		return category;
	}

	public void setCategery(String categery) {
		this.category = categery;
	}

	public productdao() {
	}

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

	public void setId(Long id) {
		this.id = id;
	}

private  List<imagesdao> images;

	public List<imagesdao> getImages() {
	return images;
}

public void setImages(List<imagesdao> myimages) {
	this.images = myimages;
}

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

	public void setReviews(List<Reviewsdao> myreviews) {
		this.reviews = myreviews;
	}

	private List<Reviewsdao> reviews;

	public List<Reviewsdao> getReviews() {
		return reviews;
	}

}
