package com.bookstore.customers.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtility {
	
	@Autowired 
	private JavaMailSender mailSender;

	public void sendEmail(String reciever , String subject , String body) {
	
		
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(reciever);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
		
	}
	
	public String setVerificationLink(String token , int otp) {
		String verificationLinkURL = "http://localhost:8001/customer/verify?otp="+otp+"&token="+token;
		return verificationLinkURL;
	}
	
}
