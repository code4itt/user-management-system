package com.store.user.management.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="likes")
public class Likes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "postid")
	private Long postId;
	
	@Column(name = "username")
	private String username;

	public Long getId() {
		return id;
	}

	public Long getPostId() {
		return postId;
	}

	public String getUsername() {
		return username;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
