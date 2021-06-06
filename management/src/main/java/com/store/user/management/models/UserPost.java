package com.store.user.management.models;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	
	/*//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Likes.class)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Likes.class)
	@JoinColumn(name = "users_post_id",referencedColumnName="id")
	@JoinColumn(name = "likes_id")
	@JoinColumn(name = "likes_username")
	private Set<Likes> likersUsername = new HashSet<>();*/
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinTable(name = "likes_db",
	joinColumns = @JoinColumn(name = "post_id",referencedColumnName="id"),
	inverseJoinColumns = @JoinColumn(name = "like_id"))
	private Set<Likes> likersusername = new HashSet<>();
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinTable(name = "comments_db",
	joinColumns = @JoinColumn(name = "post_id",referencedColumnName="id"),
	inverseJoinColumns = @JoinColumn(name = "comment_id"))
	private Set<Comments> comments = new HashSet<>();
	
	public Set<Likes> getLikersusername() {
		return likersusername;
	}

	public void setLikersusername(Set<Likes> likersUsername) {
		this.likersusername = likersUsername;
	}

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

	public Set<Comments> getComments() {
		return comments;
	}

	public void setComments(Set<Comments> comments) {
		this.comments = comments;
	}

	
	
	
}
