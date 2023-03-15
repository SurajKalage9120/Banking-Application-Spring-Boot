package org.jsp.banking_project.helper;

import org.jsp.banking_project.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.experimental.Helper;

@Service
public class MialVerification 
{
	@Autowired
	JavaMailSender mailSender;
public void sendMail(Customer customer)
{
	MimeMessage mimeMessage=mailSender.createMimeMessage();
	MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
	
	try {
		helper.setFrom("surajkalagenipani@gmail.com");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setTo(customer.getEmail());
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	    try {
			helper.setSubject("mailVerification");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    try {
			helper.setText("your otp for mail verification is"+customer.getOtp());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	   mailSender.send(mimeMessage);
}
}
