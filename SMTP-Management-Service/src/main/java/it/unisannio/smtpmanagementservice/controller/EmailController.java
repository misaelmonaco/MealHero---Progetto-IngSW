package it.unisannio.smtpmanagementservice.controller;

import it.unisannio.smtpmanagementservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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

    @PostMapping("/sendMoreThanOne")
    public ResponseEntity sendEmails(
            @RequestParam ArrayList<String> to,
            @RequestParam String subject,
            @RequestBody String text) {
        emailService.sendSimpleEmails(to, subject, text);
        return emailService.sendSimpleEmails(to, subject, text);
    }
}
