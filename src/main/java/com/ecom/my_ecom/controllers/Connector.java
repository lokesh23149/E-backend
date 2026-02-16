package com.ecom.my_ecom.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.my_ecom.entities.myentity;
import com.ecom.my_ecom.productrepository.Repository;
import com.ecom.my_ecom.resources.Resourcenotfound;






@RestController
@RequestMapping("/api/users")
public class Connector {
	@Autowired
	private Repository repo;
	@Autowired
	private PasswordEncoder password;
	
	@GetMapping
	public List<myentity> getusers() {
		return repo.findAll() ;
	}
	@PostMapping
	public myentity postdata(@RequestBody myentity us ) {
		us.setPassword(password.encode(us.getPassword()));
		if (us.getRole() == null || us.getRole().isEmpty()) {
			us.setRole("ROLE_USER");
		}
		return repo.save(us);
		
	}
	@GetMapping("/{id}")
	public myentity getbyid(@PathVariable Long id) {
		return repo.findById(id).orElseThrow(()->new Resourcenotfound("id not found in "+id));
		
	}
	@PutMapping("/{id}")
	public myentity updatedata(@PathVariable Long id ,@RequestBody myentity us) {
		myentity user =repo.findById(id).orElseThrow(()->new Resourcenotfound("id not found in "+id));
		user.setGmail(us.getGmail());
		user.setName(us.getName());
		return repo.save(user);
		
	}
	@DeleteMapping("/{id}")
	public  ResponseEntity<?> deletedata(@PathVariable Long id) {
	myentity user=	repo.findById(id).orElseThrow(()->new Resourcenotfound("Successfully deleted"));

	repo.delete(user);
	return ResponseEntity.ok().build();
		
	}
	
}
