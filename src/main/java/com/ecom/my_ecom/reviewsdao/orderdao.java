package com.ecom.my_ecom.reviewsdao;


public class orderdao {
	
	private Long referenceId;

	

	private String name;

	private String image;

	orderdao(Long productid,  String name, String image, Integer quantity, Double price) {
		super();
		this.referenceId = productid;
		
		this.name = name;
		this.image = image;
		this.quantity = quantity;
		this.price = price;
	}

	public Long getProductid() {
		return referenceId;
	}

	public void setProductid(Long productid) {
		this.referenceId = productid;
	}

	private Integer quantity;

	private Double price;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	orderdao() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public void setImage(String image) {
		this.image = image;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
