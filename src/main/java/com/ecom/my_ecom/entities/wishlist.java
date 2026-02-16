package com.ecom.my_ecom.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlist", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "user_id", "product_id" })
})
public class wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private products product;

    public wishlist() {}

    public wishlist(Long userId, products product) {
        this.userId = userId;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public products getProduct() {
        return product;
    }

    public void setProduct(products product) {
        this.product = product;
    }
}
