package com.aurionpro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.dto.EmailRequest;
import com.aurionpro.email.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmailJson(@RequestBody EmailRequest request) {
        emailService.sendSimpleEmail(request.getTo(), request.getSubject(), request.getBody());
        return ResponseEntity.ok("Email sent successfully to " + request.getTo());
    }

}
