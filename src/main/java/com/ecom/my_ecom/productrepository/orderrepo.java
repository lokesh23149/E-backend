package com.ecom.my_ecom.productrepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecom.my_ecom.entities.orders;

public interface orderrepo extends JpaRepository<orders, Long>, JpaSpecificationExecutor<orders> {

	Optional<orders> findByReferenceId(String referenceID);

	@Query("SELECT DISTINCT o FROM orders o LEFT JOIN FETCH o.orderitems oi LEFT JOIN FETCH oi.product WHERE o.referenceId = :refId")
	Optional<orders> findByReferenceIdWithItems(@Param("refId") String referenceID);

	java.util.List<orders> findByUserIdOrderByIdDesc(Long userId);

	@Query("SELECT DISTINCT o FROM orders o LEFT JOIN FETCH o.orderitems ORDER BY o.id DESC")
	java.util.List<orders> findAllWithItems();

	@Query("SELECT DISTINCT o FROM orders o LEFT JOIN FETCH o.orderitems WHERE o.userId = :userId ORDER BY o.id DESC")
	java.util.List<orders> findByUserIdWithItems(@Param("userId") Long userId);

	@Query("SELECT COALESCE(SUM(o.total), 0) FROM orders o")
	Double getTotalRevenue();

}
