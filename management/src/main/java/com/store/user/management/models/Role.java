package com.store.user.management.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer  id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "name" ,length = 20)
	private ERole name;
	/*
	public Role(ERole name) {
		this.name = name;
	}*/
/*
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
*/
	public ERole getRole() {
		return name;
	}

	public void setRole(ERole name) {
		this.name = name;
	}
	
}
