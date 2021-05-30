package com.store.user.management.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.user.management.models.Mail;
import com.store.user.management.models.User;

import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

@Service
public class OTPservice {

	@Autowired
	MailService mailService;

	private static final Integer EXPIRY_TIME_IN_MINS = 2;

	private LoadingCache<String, Integer> otpCache;

	public OTPservice() {
		super();
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRY_TIME_IN_MINS, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

	public Integer getOtp(String key) {
		try {
			return otpCache.get(key);
		} catch (Exception e) {
			return 0;
		}
	}

	public void clearOTP(String key) {
		otpCache.invalidate(key);
	}

	public String generateOtp(User user) throws MessagingException {

		String numbers = "0123456789";
		Random rndm_method = new Random();

		char[] otp = new char[6];

		for (int i = 0; i < otp.length; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		String generatedOTP= new String(otp);
		System.out.println(">>>>>>>>>>>OTP>>>>>>>"+generatedOTP);
		
		if(!generatedOTP.isEmpty()){
			  
			  Mail mail = new Mail(); 
			  //mail.setMailFrom("ig.ssyahi@gmail.com");
			  mail.setMailTo(user.getEmail()); 
			  //mail.setMailTo("ashishkmr.kaka@gmail.com");
			  mail.setSubject("OTP for Ssyahi");
			  
			  Map<String, Object> props = new HashMap<String,Object>(); 
			  props.put("sign","Ssyahi");
			  props.put("otp",generatedOTP);
			  props.put("name", user.getName());
			  mail.setProps(props);
			  
			  mailService.sendOTPEmail(mail);
		}
		
		otpCache.put(user.getUsername(), Integer.parseInt(generatedOTP));
		 
		return generatedOTP;
	}

	/*
	 * private final Supplier<String> cache = Suppliers.memoizeWithExpiration(new
	 * Supplier<String>() { public String get() {
	 * 
	 * String numbers = "0123456789"; Random rndm_method = new Random();
	 * 
	 * char[] otp = new char[6];
	 * 
	 * for (int i = 0; i < otp.length; i++) { // Use of charAt() method : to get
	 * character value // Use of nextInt() as it is scanning the value as int otp[i]
	 * = numbers.charAt(rndm_method.nextInt(numbers.length())); }
	 * 
	 * return otp.toString(); }
	 * 
	 * }, 2 , TimeUnit.MINUTES);
	 * 
	 */
	/*
	 * public ConcurrentHashMap<String,String> generateOTP(User user) throws
	 * MessagingException {
	 * 
	 * ConcurrentHashMap<String,String> otp = cache.get(user.getUsername());
	 * 
	 * if(!otp.isEmpty() || otp.containsKey(user.getUsername())) {
	 * 
	 * Mail mail = new Mail(); mail.setMailFrom("ssyahi@gmail.com");
	 * mail.setMailTo(user.getEmail()); mail.setSubject("OTP for Ssyahi");
	 * 
	 * Map<String, Object> props = new HashMap<String,Object>(); props.put("sign",
	 * "Ssyahi"); props.put("location", "Mumbai"); props.put("otp",otp);
	 * props.put("name", user.getName()); mail.setProps(props);
	 * 
	 * mailService.sendOTPEmail(mail);
	 * 
	 * return mapOtp;
	 * 
	 * }else { return null; } }
	 */

	public Boolean validateOTP(String key, Integer otp) {
		boolean flag = false;
		if(otp > 0) {
			int cachedOtp = getOtp(key);
			
		if( cachedOtp >0) {
			if(otp == cachedOtp) {
				clearOTP(key);
				flag = true;
			}
		}
		}
		return flag;
	}

}
