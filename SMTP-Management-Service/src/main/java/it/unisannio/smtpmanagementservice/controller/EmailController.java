package it.unisannio.smtpmanagementservice.controller;

import it.unisannio.smtpmanagementservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestBody String text) {
        emailService.sendSimpleEmail(to, subject, text);
        return emailService.sendSimpleEmail(to, subject, text);
    }
}
