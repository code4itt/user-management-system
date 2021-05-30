package com.store.user.management.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.store.user.management.repository.UserRepository;
import com.store.user.management.service.UserQRGenerator;

import javassist.NotFoundException;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/QR/")
@Component
public class QRCodeController {

	private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";

	@Autowired
	HttpServletResponse response;
	
	@Autowired
	UserRepository userRepo;

	@GetMapping(value = "/genrateAndDownloadQRCode/{codeText}/{width}/{height}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public void downloadInSystem(@PathVariable("codeText") String codeText, @PathVariable("width") Integer width,
			@PathVariable("height") Integer height) throws Exception {
		if(userRepo.existsByUsername(codeText)) {
		UserQRGenerator.generateQRCodeImage(codeText, width, height, QR_CODE_IMAGE_PATH);
		}else {
			throw new NotFoundException("User with username: "+codeText+" is not exist!");
		}
		}

	@GetMapping(value = "/genrateQRCode/{codeText}/{width}/{height}")
	// @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<byte[]> generateQRCode(@PathVariable("codeText") String codeText,
			@PathVariable("width") Integer width, @PathVariable("height") Integer height) throws Exception {
		
		if(userRepo.existsByUsername(codeText)) {
		return ResponseEntity.status(HttpStatus.OK).body(UserQRGenerator.getQRCodeImage(codeText, width, height));
		}else {
			throw new NotFoundException("User with username: "+codeText+" is not exist!");
		}
	}

}