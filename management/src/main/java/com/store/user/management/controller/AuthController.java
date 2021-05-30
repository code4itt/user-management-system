package com.store.user.management.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.store.user.management.models.ERole;
import com.store.user.management.models.Role;
import com.store.user.management.models.User;
import com.store.user.management.models.facebook.FacebookUser;
import com.store.user.management.payload.request.LoginRequest;
import com.store.user.management.payload.request.SignupRequest;
import com.store.user.management.payload.response.JwtResponse;
import com.store.user.management.payload.response.MessageResponse;
import com.store.user.management.repository.RoleRepository;
import com.store.user.management.repository.UserRepository;
import com.store.user.management.security.jwt.JwtUtils;
import com.store.user.management.service.FacebookClient;
import com.store.user.management.service.GeneratePasswordService;
import com.store.user.management.service.UserDetailsImpl;


@CrossOrigin(origins = "*", maxAge=3600)
@Controller
@RequestMapping("/api/auth/")
@Component
public class AuthController {

	@Autowired
	AuthenticationManager authManger;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	GeneratePasswordService genRandomPass;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	FacebookClient facebookClient;
	
	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginReq) {
		
		Authentication authentication = authManger.authenticate(
				new UsernamePasswordAuthenticationToken(loginReq.getUsername(),loginReq.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		List<String> roles =  userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getId(), userDetails.getName(),userDetails.getUsername(), userDetails.getEmail(), userDetails.getAge(), userDetails.getMobileno(),
				roles));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpReq) {
		
		if(userRepo.existsByUsername(signUpReq.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if(userRepo.existsByEmail(signUpReq.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: This Email is already registered!"));
		}
		
		User user = new User(signUpReq.getUsername(), signUpReq.getEmail(),
				encoder.encode(signUpReq.getPassword()), signUpReq.getName(),
				signUpReq.getAge(), signUpReq.getMobileno());
		
		Set<String> strRoles = signUpReq.getRoles();
		Set<Role> roles = new HashSet<>();
		
		
		if(strRoles.isEmpty()) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(userRole);
		}else {
			strRoles.forEach(role -> {
				switch(role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(adminRole);
					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(modRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		userRepo.save(user);
		
		return ResponseEntity.ok(new MessageResponse("User registered Successfully"));
	}
	
	@PostMapping("/facebook/login/")
	public ResponseEntity<?> facebookRegisterUser(@RequestBody SignupRequest user,@RequestParam String dob) throws ParseException, MessagingException {
		
		//System.out.println("Access Token ========>>>>>"+accessToken);
		
		//FacebookUser facebookUser = facebookClient.getUser(accessToken);
		//System.out.println("User Details  ========>>>>>"+facebookUser.getEmail()+"  "+facebookUser.getFirstName()+"  "+facebookUser.getBirthday());
		if(!userRepo.existsByUsername(user.getUsername())){
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	    Date date = formatter.parse(dob);
	    //Converting obtained Date object to LocalDate object
	    Instant instant = date.toInstant();
	    ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
	    LocalDate givenDate = zone.toLocalDate();
	    //Calculating the difference between given date to current date.
	    Period period = Period.between(givenDate, LocalDate.now());
	    
	    String randomGeneratedPassword = genRandomPass.mailGeneratedPassword(user.getName(), user.getEmail(),user.getUsername());
	    
	    /*String tempPasswordForLogin = randomGeneratedPassword;
	    LoginRequest lr = new LoginRequest();
		lr.setUsername(user.getUsername());
		lr.setPassword(tempPasswordForLogin);
		*/
	    user.setAge(period.getYears());
	    user.setPassword(randomGeneratedPassword);
	    /*
		System.out.println("User Details  ========>>>>>"+facebookUser.getEmail()+"  "+facebookUser.getFirstName()+"  "+facebookUser.getBirthday());
		System.out.println("User Details  ========>>>>>"+facebookUser.toString());
		SignupRequest signUpRequest = new SignupRequest();
		signUpRequest.setEmail(facebookUser.getEmail());
		signUpRequest.setAge(period.getYears());
		signUpRequest.setMobileno("0000000000");
		signUpRequest.setName(facebookUser.getFirstName()+" "+facebookUser.getLastName());
		signUpRequest.setPassword(randomGeneratedPassword);
		signUpRequest.setUsername(facebookUser.getId());
		*/
		registerUser(user);
		
		//ResponseEntity<JwtResponse> jwtResponse =  authenticateUser(lr);
        
		return ResponseEntity.ok("You are registered Successfully! Your id and password is sent on your mail, Kindly login using that!");
		}else {
			return ResponseEntity.ok(null);
		}
	}
	
	
	
}
