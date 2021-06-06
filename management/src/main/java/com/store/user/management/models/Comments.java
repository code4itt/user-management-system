package com.store.user.management.models;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="comments")
public class Comments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "postid")
	private Long postId;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "comment")
	private String comment;
	
	private LocalDateTime commentedAt = ZonedDateTime.now().toLocalDateTime();
	
	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getComment() {
		return comment;
	}

	public LocalDateTime getCommentedAt() {
		return commentedAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setCommentedAt(LocalDateTime commentedAt) {
		this.commentedAt = commentedAt;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}
	
	
}
