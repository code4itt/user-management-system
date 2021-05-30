package com.store.user.management.controller;

import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.store.user.management.models.User;
import com.store.user.management.repository.UserRepository;
import com.store.user.management.service.OTPservice;

@CrossOrigin(origins = "*", maxAge=3600)
@Controller
@RequestMapping("/api/otp/")
@Component
public class OTPContoller {
	
	@Autowired
	OTPservice otpService;
	
	@Autowired
	UserRepository userRepo;

		@PostMapping("/generateOTP")
		@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
		public ResponseEntity<?> generateOtp(@RequestParam("user_id") Long id) throws MessagingException {
			Optional<User> user = userRepo.findById(id);
			System.out.print(user.get().getUsername());
			otpService.generateOtp(user.get());

			return ResponseEntity.ok("OTP generated successfully and sent to your mail id");
			
		}
		
		@PostMapping("/validateOTP")
		@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
		public ResponseEntity<Boolean> validateOtp(@RequestParam("enteredOtp") Integer otp) {
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			System.out.println(username + "      " +otp);
			System.out.println();
			return ResponseEntity.ok(otpService.validateOTP(username, otp));
			
		}
}
