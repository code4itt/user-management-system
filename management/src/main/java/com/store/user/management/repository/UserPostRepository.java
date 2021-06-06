package com.store.user.management.repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.user.management.models.Likes;
import com.store.user.management.models.User;
import com.store.user.management.models.UserPost;

@Repository("userPostRepository")
public interface UserPostRepository  extends PagingAndSortingRepository<UserPost,Long>{


	@Query(
		value=	"SELECT * FROM user_management_db.users_post where id=:id",
		nativeQuery = true)
	List<UserPost> getLikesByPostId(@Param("id") long postid);
	
	
		@Query(
			value=	"SELECT * FROM user_management_db.users_post where user_name=:username",
			nativeQuery = true)
		List<UserPost> getPostListByUserName(@Param("username") String username,Pageable page);
		
		@Modifying
		@Query(
				value=	"UPDATE user_management_db.users_post SET post =:post, posted_at =:postedAt WHERE (id = :id)",
				nativeQuery = true)
		void updatePost(@Param("post") String post,@Param("id") long id,@Param("postedAt") Instant postedAt);
		
		@Modifying
		@Query(
				value=	"UPDATE user_management_db.likes_db SET post_id =:id, liker_username=:username",
				nativeQuery = true)
		void updateLikersUsername(@Param("id") long id,@Param("username") long username);
		
		@Modifying
		@Query(
				value=	"INSERT INTO user_management_db.likes_db (post_id, like_id) VALUES (:postid, :likeid)",
				nativeQuery = true)
		void saveLikesDatabase(@Param("postid") long id,@Param("likeid") long likeid);
		
		@Modifying
		@Query(
				value=	"INSERT INTO user_management_db.comments_db (post_id, comment_id) VALUES (:postid, :commentid)",
				nativeQuery = true)
		void saveJoinCommentDatabase(@Param("postid") long id,@Param("commentid") long commentid);
		
		@Modifying
		@Query(
				value=	"DELETE FROM user_management_db.likes_db WHERE like_id IN (SELECT id FROM user_management_db.likes WHERE (postid= :id AND username = :usernames))",
				nativeQuery = true)
		void deleteLike(@Param("id") long id,@Param("usernames") String username);
}
