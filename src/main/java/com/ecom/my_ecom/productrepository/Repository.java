package com.ecom.my_ecom.productrepository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.my_ecom.entities.myentity;

public interface Repository extends JpaRepository<myentity, Long>{
	
	Optional<myentity> findByusername (String username);

}
