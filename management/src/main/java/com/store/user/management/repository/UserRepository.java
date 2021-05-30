package com.store.user.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.user.management.models.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User,Long> {
	Optional<User> findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	
	
	@Query(
		value=	"SELECT * FROM user_management_db.users where id IN (SELECT user_id FROM user_management_db.user_roles WHERE role_id IN (SELECT id FROM user_management_db.roles where name=:roles))",
		nativeQuery = true)
	List<User> getUserListByRoleName(@Param("roles") String userRole);
	
	@Modifying
	@Query(
			value=	"UPDATE user_management_db.users SET age =:age, email =:email, mobileno=:mobileno, password=:password WHERE (id = :id)",
			nativeQuery = true)
		void updateUser(@Param("age") int age,@Param("email") String email,@Param("mobileno") String mobileno,@Param("password") String password,@Param("id") long id);
	
}
