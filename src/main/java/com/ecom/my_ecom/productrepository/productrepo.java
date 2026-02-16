package com.ecom.my_ecom.productrepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ecom.my_ecom.entities.products;

// this is used for communicating with database and other tools which useing like a postman and browser...etc
public interface productrepo extends JpaRepository<products, Long> ,JpaSpecificationExecutor<products>{

	@Query("SELECT DISTINCT p.category FROM products p WHERE p.category IS NOT NULL ORDER BY p.category")
	List<String> findDistinctCategories();

}
