package com.store.user.management.repository;

import org.springframework.stereotype.Repository;

import com.store.user.management.models.ERole;
import com.store.user.management.models.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role,Integer> {
	Optional<Role> findByName(ERole name);
}
