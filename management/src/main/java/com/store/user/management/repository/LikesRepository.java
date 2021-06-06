package com.store.user.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.user.management.models.Likes;

@Repository("postLikesRepository")
public interface LikesRepository  extends PagingAndSortingRepository<Likes,Long>{

	/*
		@Query(
			value=	"SELECT likers_username FROM user_management_db.likes where post_id=:postid",
			nativeQuery = true)
		List<String> getLikesByPostId(@Param("id") long postid);
		
		
		@Modifying
		@Query(
				value=	"DELETE * FROM user_management_db.likes WHERE (post_id= :id && username = :usernames)",
				nativeQuery = true)
		void deleteLikersUsername(@Param("id") long id,@Param("usernames") String username);
		*/
	
	@Query(
			value=	"SELECT username FROM user_management_db.likes where postid=:id",
			nativeQuery = true)
		List<String> getLikesByPostId(@Param("id") long postid);
	

	@Modifying
	@Query(
			value=	"DELETE FROM user_management_db.likes WHERE (postid= :id AND username = :usernames)",
			nativeQuery = true)
	void deleteLikersUsername(@Param("id") long id,@Param("usernames") String username);
	
	
	@Query(
			value=	"SELECT id FROM user_management_db.likes WHERE (postid= :id AND username = :usernames)",
			nativeQuery = true)
	Long getLikeIdWithPostIdAndUsername(@Param("id") long postid,@Param("usernames") String username);
	
	
	
	
	
	
}
