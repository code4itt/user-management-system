package com.store.user.management.payload.response;

import java.util.List;

public class JwtResponse {
	
	private String accessToken;
	
	private String type = "Bearer";
	
	private Long id;
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String username;
	
	private String email;
	
	private int age;
	
	private String mobileno;
	
	private List<String> roles;

	public JwtResponse(String accessToken, Long id, String name,String username, String email, int age, String mobileno,
			List<String> roles) {
		super();
		this.name=name;
		this.accessToken = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.age = age;
		this.mobileno = mobileno;
		this.roles = roles;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	
}
