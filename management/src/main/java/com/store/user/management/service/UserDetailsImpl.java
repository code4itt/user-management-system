package com.store.user.management.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.user.management.models.User;

@Service
public class UserDetailsImpl implements UserDetails{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String username;
	
	private String name;
	
	private String email;
	
	private int age;
	
	private String mobileno;
	
	@JsonIgnore
	private String password;

	public Collection<? extends GrantedAuthority> authorities;

	UserDetailsImpl(){}
	

	public UserDetailsImpl(Long id, String username, String name, String email, int age, String mobileno,
			String password, Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
		this.email = email;
		this.age = age;
		this.mobileno = mobileno;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getRole().name()))
				.collect(Collectors.toList());
		
		return new UserDetailsImpl(
				user.getId(),
				user.getUsername(),
				user.getName(),
				user.getEmail(),
				user.getAge(),
				user.getMobileno(),
				user.getPassword(),
				authorities
				);
				
	}


	@Override
	public String getPassword() {
		
		return password;
	}



	@Override
	public String getUsername() {
		
		return username;
	}



	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}



	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}



	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}



	@Override
	public boolean isEnabled() {
		
		return true;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return authorities;
	}


	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}

	public String getMobileno() {
		return mobileno;
	}


	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
					
	}
	
	
	
}
