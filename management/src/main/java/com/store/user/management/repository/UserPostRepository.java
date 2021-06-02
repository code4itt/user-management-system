package com.store.user.management.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.user.management.models.UserPost;

@Repository("userPostRepository")
public interface UserPostRepository  extends PagingAndSortingRepository<UserPost,Long>{

		@Query(
			value=	"SELECT * FROM user_management_db.users_post where user_name=:username",
			nativeQuery = true)
		List<UserPost> getPostListByUserName(@Param("username") String username,Pageable page);
		
		@Modifying
		@Query(
				value=	"UPDATE user_management_db.users_post SET post =:post, posted_at =:postedAt WHERE (id = :id)",
				nativeQuery = true)
		void updatePost(@Param("post") String post,@Param("id") long id,@Param("postedAt") Instant postedAt);
		
}
