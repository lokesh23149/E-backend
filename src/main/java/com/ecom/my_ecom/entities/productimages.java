package com.ecom.my_ecom.entities;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class productimages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String publicid;
	private String url;
	public productimages(long id, String publicid, String url) {
		super();
		this.id = id;
		this.publicid = publicid;
		this.url = url;
	}
	productimages() {
		super();
		// TODO Auto-generated constructor stub
	}
	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnore
	
	private products Product;

	

	public productimages(String url, products product) {
		// TODO Auto-generated constructor stub
		this.url="upload"+url;
		this.publicid=url;
		this.Product=product;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPublicid() {
		return publicid;
	}

	public products getProduct() {
		return Product;
	}

	public void setProduct(products product) {
		Product = product;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPublicid(String publicid) {
		this.publicid = publicid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		
	}
}
