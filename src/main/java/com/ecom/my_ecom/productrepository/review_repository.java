package com.ecom.my_ecom.productrepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecom.my_ecom.entities.productreviews;

public interface review_repository extends JpaRepository<productreviews, Long> {

	@Query("SELECT r FROM productreviews r WHERE r.Productreviews.id = :productId ORDER BY r.id DESC")
	List<productreviews> findByProductIdOrderByIdDesc(@Param("productId") Long productId);

}
