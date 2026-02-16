package com.ecom.my_ecom.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class myentity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}



	private String name;
	private String gmail;
	private String username;
	private String password;
	private String role = "ROLE_USER";
	private String phone;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
	}

	
	
	public myentity(String name, String gmail ,long id,String passwString, String useString) {
		super();
		this.id=id;
		this.name = name;
		this.gmail = gmail;
		this.username=useString;
		this.password=passwString;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role != null ? role : "ROLE_USER";
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }
	public String getState() { return state; }
	public void setState(String state) { this.state = state; }
	public String getZipCode() { return zipCode; }
	public void setZipCode(String zipCode) { this.zipCode = zipCode; }

	public myentity() {
		
	}
	

}
