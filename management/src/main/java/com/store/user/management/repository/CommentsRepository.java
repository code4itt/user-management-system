package com.store.user.management.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.user.management.models.Comments;

@Repository("postCommentsRepository")
public interface CommentsRepository extends PagingAndSortingRepository<Comments, Long> {
	
	@Query(
			value=	"SELECT * FROM user_management_db.comments where postid=:postId",
			nativeQuery = true)
		List<Comments> getCommentsByPostId(@Param("postId") long postId,Pageable page);
		
		@Modifying
		@Query(
				value=	"UPDATE user_management_db.comments SET comment =:comment, commented_at =:commentedAt WHERE (id = :id)",
				nativeQuery = true)
		void updateComment(@Param("comment") String comment,@Param("id") long id,@Param("commentedAt") LocalDateTime postedAt);
		
}
