package com.aurionpro.email;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper; // Necessary for attachments
import org.springframework.stereotype.Service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo(to);
        m.setSubject(subject);
        m.setText(body);
        mailSender.send(m);
    }

   
    public void sendEmailWithAttachment(String toEmail, String subject, String body, byte[] attachmentBytes,
            String attachmentFilename) {
        
        MimeMessage message = mailSender.createMimeMessage();
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, false);
            
            ByteArrayResource resource = new ByteArrayResource(attachmentBytes);
            
            helper.addAttachment(
                attachmentFilename,   
                resource,             
                "application/pdf"     
            );
            
            mailSender.send(message);
            System.out.println("Email sent successfully with attachment: " + attachmentFilename);

        } catch (MessagingException e) {
            System.err.println("Error sending email with attachment: " + e.getMessage());
            throw new RuntimeException("Failed to send email with attachment.", e); 
        }
    }

}