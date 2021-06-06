package com.store.user.management.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.user.management.models.Comments;
import com.store.user.management.models.Likes;
import com.store.user.management.models.User;
import com.store.user.management.models.UserPost;
import com.store.user.management.repository.CommentsRepository;
import com.store.user.management.repository.LikesRepository;
import com.store.user.management.repository.RoleRepository;
import com.store.user.management.repository.UserPostRepository;
import com.store.user.management.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge=3600)
@RestController
@RequestMapping("/api/post")
public class UploadPostsController {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	LikesRepository likeRepo;
	
	@Autowired
	CommentsRepository commentRepo;
	
	@Autowired
	UserPostRepository postRepo;
	
	@PostMapping("/upload")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> uploadPost(@RequestBody UserPost post) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth.getName().equals(post.getUserName())) {
			post.setPostedAt(Instant.now());
			postRepo.save(post);
			return ResponseEntity.ok("Post Uploaded Successfully!!");
		}else {
			return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
		}
		
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public List<UserPost> getAllPost(@RequestParam int page) {
		Pageable req = PageRequest.of(page, 5, Sort.by("id").descending());
		return postRepo.findAll(req).toList();
	}
	
	@Transactional
	@PutMapping("/update")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> updatePost(@RequestBody UserPost post) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth.getName().equals(post.getUserName())) {
			post.setPostedAt(Instant.now());
			postRepo.updatePost(post.getPost(), post.getId(), post.getPostedAt());
			return ResponseEntity.ok("Post Updated!!");
		}else {
			return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
		}
	
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public List<UserPost> getPostByUsername(@RequestParam("username") String username,@RequestParam("page") int page) {
		Pageable req = PageRequest.of(page, 5, Sort.by("id").descending());
		return postRepo.getPostListByUserName(username,req);
	
	}
	
	@GetMapping("/{postid}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public UserPost getPostByPostId(@PathVariable("postid") Long postid) {

		return postRepo.findById(postid).get();
	}
	
	@DeleteMapping("/delete/{postid}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> deletePost(@RequestParam String username,@PathVariable("postid") Long postid) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.getName().equals(username)) {
			postRepo.deleteById(postid);
			return ResponseEntity.ok("Post Deleted Successfully!!");
		}else
		{
			boolean isAdmin = auth.getAuthorities().stream()
			          .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
			if(isAdmin) {
				postRepo.deleteById(postid);
				return ResponseEntity.ok("Post Deleted Successfully!!");
			}else {
				return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
			}
		}
		
	}
	
	
	@Transactional
	@PutMapping("/comment")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> postComment(@RequestBody Comments comment) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth.getName().equals(comment.getUsername())) {
			
			comment.setCommentedAt(LocalDateTime.now());
			commentRepo.save(comment);
			postRepo.saveJoinCommentDatabase(comment.getPostId(), comment.getId());
			
			return ResponseEntity.ok(postRepo.findById(comment.getPostId()));
		}else {
			return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
		}
	
	}
	
	@GetMapping("/getComments")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public List<Comments> getCommentsWithPostId(@RequestParam long postId,@RequestParam int page) {
		Pageable req = PageRequest.of(page, 5, Sort.by("id").descending());
		
		return commentRepo.getCommentsByPostId(postId, req);
	}
	
	@GetMapping("/getlikes")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public List<String> getUsernameOfLikes(@RequestParam long postId) {
		
		return likeRepo.getLikesByPostId(postId);
		
	}
	
	@Transactional
	@PutMapping("/update/like")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> updateLike(@RequestBody Likes likeData) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(likeData.getUsername() + likeData.getPostId());
		if(auth.getName().equals(likeData.getUsername())) {
		
			System.out.println(likeRepo.getLikeIdWithPostIdAndUsername(likeData.getPostId(), likeData.getUsername()));
			if(likeRepo.getLikeIdWithPostIdAndUsername(likeData.getPostId(), likeData.getUsername())==null) {
			likeRepo.save(likeData);
			
			postRepo.saveLikesDatabase(likeData.getPostId(), likeData.getId());
			
			return ResponseEntity.ok(postRepo.findById(likeData.getPostId()));
			}else {
				return ResponseEntity.ok("You already liked post!!");
			}
		}else {
			return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
		}
	
	}
	
	@Transactional
	@PutMapping("/update/unlike")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> updateUnliked(@RequestBody Likes likeData) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(likeRepo.getLikeIdWithPostIdAndUsername(likeData.getPostId(), likeData.getUsername()));
		if(auth.getName().equals(likeData.getUsername())) {
			if(likeRepo.getLikeIdWithPostIdAndUsername(likeData.getPostId(), likeData.getUsername())!=null)
			{
			
			postRepo.deleteLike(likeData.getPostId(), likeData.getUsername());
			
			likeRepo.deleteById(likeRepo.getLikeIdWithPostIdAndUsername(likeData.getPostId(), likeData.getUsername()).longValue());
			return ResponseEntity.ok(postRepo.findById(likeData.getPostId()));
			}else {
				return ResponseEntity.ok("Invalid attempt!! You already didnt like this post!!");
			}
		}else {
			return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
		}
	
	}
	
}
