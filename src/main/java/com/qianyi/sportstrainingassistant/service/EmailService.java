
package com.qianyi.sportstrainingassistant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String senderEmail;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String senderEmail
    ) {
        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
    }

    public void sendEmail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
