package com.ecom.my_ecom.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<orderitems> orderitems = new ArrayList<>();

    private String status;
    private Double orderitemtotal;
    private Double gst;
    private Double total;
    private String referenceId;

    orders(long id, List<com.ecom.my_ecom.entities.orderitems> orderitems, String status, Double orderitemtotal,
			Double gst, Double total, String ordernum) {
		super();
		this.id = id;
		this.orderitems = orderitems;
		this.status = status;
		this.orderitemtotal = orderitemtotal;
		this.gst = gst;
		this.total = total;
		this.referenceId = ordernum;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getOrderitemtotal() {
		return orderitemtotal;
	}

	public void setOrderitemtotal(Double orderitemtotal) {
		this.orderitemtotal = orderitemtotal;
	}

	public Double getGst() {
		return gst;
	}

	public void setGst(Double gst) {
		this.gst = gst;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	@JsonProperty("referenceId")
	public String getreferenceID() {
		return referenceId;
	}

	public void setreferenceId(String ordernum) {
		this.referenceId = ordernum;
	}

	public orders() {}

    public List<orderitems> getOrderitems() {
        return orderitems;
    }

	public void setOrderitems(List<orderitems> orderitems) {
		this.orderitems = orderitems;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	private java.time.Instant createdAt;

	public java.time.Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(java.time.Instant createdAt) {
		this.createdAt = createdAt;
	}
}

