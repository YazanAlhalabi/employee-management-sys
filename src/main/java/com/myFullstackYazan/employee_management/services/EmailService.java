package com.myFullstackYazan.employee_management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  @Autowired
  private JavaMailSender mailSender;

  @Value("${backend.origin}")
  private String ORIGIN;
  @Value("${GMAIL_APP_USERNAME}")
  private String from;

  public void sendAccountCreationEmail(String to, String token) {
    String link = ORIGIN + "?token=" + token;
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setTo(to);
    message.setSubject("Create Your Account!");
    message.setText("Hi! Please create your account using this link below: \n" + link);
    mailSender.send(message);
  }
}
