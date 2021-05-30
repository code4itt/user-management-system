package com.store.user.management.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.user.management.models.Mail;

@Service
public class GeneratePasswordService {

	@Autowired
	MailService mailService;
	
	private String generatePassword() {
		System.out.println("Generating password using random() : ");
        
  
        // A strong password has Cap_chars, Lower_chars,
        // numeric value and symbols. So we are using all of
        // them to generate our password
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*_=+-/.?<>)";
  
  
        String values = Capital_chars + Small_chars +
                        numbers + symbols;
  
        // Using random method
        Random rndm_method = new Random();
  
        char[] password = new char[12];
  
        for (int i = 0; i < 12; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            password[i] =
              values.charAt(rndm_method.nextInt(values.length()));
  
        }
        System.out.print(password);
        return new String(password);
    }
	
	public String mailGeneratedPassword(String user_name,String user_email,String username) throws MessagingException{
		 String generatedPassword = generatePassword();
		  Mail mail = new Mail();
		  mail.setMailTo(user_email);
		  mail.setSubject("Password for Ssyahi");
		  
		  Map<String, Object> props = new HashMap<String,Object>(); 
		  props.put("sign","Ssyahi");
		  props.put("password",generatedPassword);
		  props.put("name", user_name);
		  props.put("username", username);
		  mail.setProps(props);
		  
		  mailService.sendPasswordMail(mail);

		  return generatedPassword;
	}
	
}
