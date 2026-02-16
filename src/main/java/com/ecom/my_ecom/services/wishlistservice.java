package com.ecom.my_ecom.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.my_ecom.entities.products;
import com.ecom.my_ecom.entities.wishlist;
import com.ecom.my_ecom.productrepository.productrepo;
import com.ecom.my_ecom.productrepository.wishlistrepo;
import com.ecom.my_ecom.reviewsdao.imagesdao;
import com.ecom.my_ecom.reviewsdao.productdao;

@Service
public class wishlistservice {

    @Autowired
    private wishlistrepo wishlistRepo;

    @Autowired
    private productrepo productRepo;

    @Autowired
    private product_service productService;

    public List<Map<String, Object>> getWishlistByUserId(Long userId) {
        List<wishlist> items = wishlistRepo.findByUserIdOrderByIdDesc(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (wishlist w : items) {
            products p = w.getProduct();
            if (p != null) {
                productdao pd = productService.getProductByIdAsDto(p.getId());
                Map<String, Object> item = new HashMap<>();
                item.put("id", w.getId());
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", pd.getId());
                productMap.put("name", pd.getName());
                productMap.put("price", pd.getPrice());
                productMap.put("description", pd.getDiscripsion());
                productMap.put("discripsion", pd.getDiscripsion());
                if (pd.getImages() != null && !pd.getImages().isEmpty()) {
                    List<Map<String, Object>> imgs = new ArrayList<>();
                    for (imagesdao img : pd.getImages()) {
                        Map<String, Object> m = new HashMap<>();
                        m.put("url", img.getUrl());
                        imgs.add(m);
                    }
                    productMap.put("images", imgs);
                } else {
                    productMap.put("images", List.of());
                }
                item.put("product", productMap);
                result.add(item);
            }
        }
        return result;
    }

    public Map<String, Object> addToWishlist(Long userId, Long productId) {
        products product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        if (wishlistRepo.existsByUserIdAndProduct_Id(userId, productId)) {
            return Map.of("message", "Already in wishlist");
        }

        wishlist w = new wishlist(userId, product);
        wishlist saved = wishlistRepo.save(w);

        productdao pd = productService.getProductByIdAsDto(productId);
        Map<String, Object> item = new HashMap<>();
        item.put("id", saved.getId());
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("id", pd.getId());
        productMap.put("name", pd.getName());
        productMap.put("price", pd.getPrice());
        productMap.put("description", pd.getDiscripsion());
        if (pd.getImages() != null && !pd.getImages().isEmpty()) {
            List<Map<String, Object>> imgs = new ArrayList<>();
            for (imagesdao img : pd.getImages()) {
                Map<String, Object> m = new HashMap<>();
                m.put("url", img.getUrl());
                imgs.add(m);
            }
            productMap.put("images", imgs);
        } else {
            productMap.put("images", List.of());
        }
        item.put("product", productMap);
        return item;
    }

    public void removeFromWishlist(Long userId, Long productId) {
        wishlistRepo.deleteByUserIdAndProduct_Id(userId, productId);
    }

    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistRepo.existsByUserIdAndProduct_Id(userId, productId);
    }
}
