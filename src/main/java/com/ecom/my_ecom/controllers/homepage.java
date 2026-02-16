package com.ecom.my_ecom.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class homepage {
	
	@GetMapping("/")
	public String homepages() {
		return "welcome to home page"; 
		
	}
	@GetMapping("/dashboad")
	public String loginsuccess() {
		return " login successfully" ;
	}

}
