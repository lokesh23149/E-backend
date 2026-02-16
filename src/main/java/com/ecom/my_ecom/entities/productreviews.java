package com.ecom.my_ecom.entities;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class productreviews {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Min(value = 1)
	@Max(value = 5)
	private Double ratings;

	public productreviews(Long id, @Min(1) @Max(5) Double ratings, String comments) {
		super();
		this.id = id;
		this.ratings = ratings;
		this.comments = comments;
	}

	public productreviews() {
		super();
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getRatings() {
		return ratings;
	}

	public void setRatings(Double ratings) {

		this.ratings = ratings;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	

	private String comments;

	private java.time.Instant createdAt;

	@ManyToOne
	@JoinColumn(name = "product_id")
	
	private products Productreviews;

	public java.time.Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(java.time.Instant createdAt) {
		this.createdAt = createdAt;
	}
	public products getProductreviews() {
		return Productreviews;
	}

	

	public void setProductreviews(products addproduct) {
		Productreviews = addproduct;
	}
	

}
