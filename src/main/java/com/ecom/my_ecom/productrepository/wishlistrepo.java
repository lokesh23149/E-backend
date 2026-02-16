package com.ecom.my_ecom.productrepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.my_ecom.entities.wishlist;

public interface wishlistrepo extends JpaRepository<wishlist, Long> {

    List<wishlist> findByUserIdOrderByIdDesc(Long userId);

    Optional<wishlist> findByUserIdAndProduct_Id(Long userId, Long productId);

    boolean existsByUserIdAndProduct_Id(Long userId, Long productId);

    void deleteByUserIdAndProduct_Id(Long userId, Long productId);
}
