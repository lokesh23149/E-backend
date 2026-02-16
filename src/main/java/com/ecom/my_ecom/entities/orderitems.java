package com.ecom.my_ecom.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
public class orderitems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "referenceId")
    @JsonIgnore
    private products product;

    private String name;
    private String image;
    private Integer quantity;

    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id")
    private orders order;

    public orderitems() {}

    orderitems(long id, Double price, products product, String name, String image, Integer quantity, orders order) {
		super();
		this.id = id;
		this.price = price;
		this.product = product;
		this.name = name;
		this.image = image;
		this.quantity = quantity;
		this.order = order;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public products getProduct() {
		return product;
	}

	public void setProduct(products product) {
		this.product = product;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
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

	public void setOrder(orders order) {
        this.order = order;
    }

	public orders getOrder() {
        return order;
    }

    @JsonProperty("productId")
    public Long getProductId() {
        return product != null ? product.getId() : null;
    }
}
