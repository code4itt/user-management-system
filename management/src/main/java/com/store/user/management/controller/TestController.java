package com.store.user.management.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.user.management.models.ERole;
import com.store.user.management.models.Role;
import com.store.user.management.models.User;
import com.store.user.management.repository.RoleRepository;
import com.store.user.management.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge=3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RoleRepository roleRepo;
	
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public List<User> userAccess() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<User> loggedInUser = userRepo.findByUsername(auth.getName());
		List<User> user = new ArrayList<User>();
		user.add(loggedInUser.get());
		return user;
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> adminAccess() {
		
		return userRepo.findAll();
		//return "Admin Board.";
	}
	
	@Transactional
	@PutMapping("/user/update")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public void updateYourDetails(@RequestBody User saveuser) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = auth.getAuthorities().stream()
		          .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		
		if(isAdmin) {
			saveuser.setPassword(encoder.encode(saveuser.getPassword()));
			userRepo.updateUser(saveuser.getAge(), saveuser.getEmail(), saveuser.getMobileno(), saveuser.getPassword(), saveuser.getId());
		}else
		{
		if(auth.getName().equals(saveuser.getUsername())) {
			saveuser.setPassword(encoder.encode(saveuser.getPassword()));
			userRepo.updateUser(saveuser.getAge(), saveuser.getEmail(), saveuser.getMobileno(), saveuser.getPassword(), saveuser.getId());
		}else {
			throw new UnauthorizedClientException("You are not have right (401)");
		}
		}
	
	}
	
	@DeleteMapping("/admin/user/delete/{userid}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteUser(@PathVariable Long userid) {
		//if(roleRepo.existsById(userid.intValue()))
		//roleRepo.deleteById(userid.intValue());
		userRepo.deleteById(userid);
		//return "Admin Board.";
	}
	
	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public List<User> moderatorAccess() {
	
		
		return userRepo.getUserListByRoleName("ROLE_USER");
	}
	
	@GetMapping("/user/getUser/{username}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getOwnDetails(@PathVariable String username) throws Exception {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		boolean isAdmin = auth.getAuthorities().stream()
		          .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		
		Optional<User> user=null;
		if(isAdmin) {
			user = userRepo.findByUsername(username);
		}else
		{
		if(auth.getName().equals(username)) {
		user = userRepo.findByUsername(username);
		
		}else {
			throw new UnauthorizedClientException("You are not have right (401)");
		}
		}
		
		return ResponseEntity.ok(user.get());
	}
}

/*
if(isAdmin) {

User user = new User(saveuser.getUsername(), saveuser.getEmail(),
		encoder.encode(saveuser.getPassword()), saveuser.getName(),
		saveuser.getAge(), saveuser.getMobileno());

Set<String> strRoles = saveuser.getRoles();
Set<Role> roles = new HashSet<>();


if(strRoles.isEmpty()) {
	Role userRole = roleRepo.findByName(ERole.ROLE_USER)
			.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
	roles.add(userRole);
}else {
	strRoles.forEach(role -> {
		switch(role) {
		case "admin":
			Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
			.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(adminRole);
			break;
		case "mod":
			Role modRole = roleRepo.findByName(ERole.ROLE_MODERATOR)
			.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(modRole);
			break;
		default:
			Role userRole = roleRepo.findByName(ERole.ROLE_USER)
			.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(userRole);
		}
	});
}
user.setRoles(roles);
userRepo.save(user);
}else
{
if(auth.getName().equals(saveuser.getUsername())) {

User user = new User(saveuser.getUsername(), saveuser.getEmail(),
		encoder.encode(saveuser.getPassword()), saveuser.getName(),
		saveuser.getAge(), saveuser.getMobileno());

Set<String> strRoles = saveuser.getRoles();
Set<Role> roles = new HashSet<>();


if(strRoles.isEmpty()) {
	Role userRole = roleRepo.findByName(ERole.ROLE_USER)
			.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
	roles.add(userRole);
}else {
	strRoles.forEach(role -> {
		switch(role) {
		case "admin":
			Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
			.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(adminRole);
			break;
		case "mod":
			Role modRole = roleRepo.findByName(ERole.ROLE_MODERATOR)
			.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(modRole);
			break;
		default:
			Role userRole = roleRepo.findByName(ERole.ROLE_USER)
			.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(userRole);
		}
	});
}
user.setRoles(roles);
userRepo.save(user);
}else {
throw new UnauthorizedClientException("You are not have right (401)");
}
}
*/