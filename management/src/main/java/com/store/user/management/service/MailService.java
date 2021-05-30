package com.store.user.management.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.store.user.management.models.Mail;

@Service
public class MailService {

	 @Autowired
	 private JavaMailSender emailSender;

	 @Autowired
	 private SpringTemplateEngine templateEngine;
	 
	 @Value("${spring.mail.username}")
	 private String mailFrom;
	 
	 public void sendOTPEmail(Mail mail) throws MessagingException {
		 	MimeMessage message = emailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message,
	                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
	                StandardCharsets.UTF_8.name());
	        //helper.addAttachment("template-cover.png", new ClassPathResource("javabydeveloper-email.PNG"));
	        Context context = new Context();
	        context.setVariables(mail.getProps());
	    
	        String html = templateEngine.process("OTPmail", context);
	        helper.setTo(mail.getMailTo());
	        helper.setText(html, true);
	        helper.setSubject(mail.getSubject());
	        helper.setFrom(mailFrom);
	        
	        emailSender.send(message);
	 }
	 
	 
	 public void sendPasswordMail(Mail mail) throws MessagingException {
		 	MimeMessage message = emailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message,
	                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
	                StandardCharsets.UTF_8.name());
	        //helper.addAttachment("template-cover.png", new ClassPathResource("javabydeveloper-email.PNG"));
	        Context context = new Context();
	        context.setVariables(mail.getProps());
	    
	        String html = templateEngine.process("PASSWORD_mail", context);
	        helper.setTo(mail.getMailTo());
	        helper.setText(html, true);
	        helper.setSubject(mail.getSubject());
	        helper.setFrom(mailFrom);
	        
	        emailSender.send(message);
	 }
	
	
}
