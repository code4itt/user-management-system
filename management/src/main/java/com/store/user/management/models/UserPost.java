package com.store.user.management.models;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="users_post")
public class UserPost {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
	private Instant postedAt = Instant.now();
	
	@NotBlank
	@Size(min =10 ,max =500)
	private String post;
	
	@NotBlank
	private String userName;

	public long getId() {
		return id;
	}

	public Instant getPostedAt() {
		return postedAt;
	}

	public String getPost() {
		return post;
	}

	public String getUserName() {
		return userName;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setPostedAt(Instant postedAt) {
		this.postedAt = postedAt;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
	
}
