package it.unisannio.smtpmanagementservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.regex.Pattern;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public ResponseEntity sendSimpleEmails(ArrayList<String> to, String subject, String text) {
        for(String toEmail : to) {
            if(!isValidEmail(toEmail))
                return ResponseEntity.badRequest().body("Indirizzo email non valido!");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to.toArray(new String[to.size()]));
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
        return ResponseEntity.status(HttpStatus.CREATED).body("Email inviata con successo all'indirizzo: " + to);
    }

    public ResponseEntity sendSimpleEmail(String to, String subject, String text) {
        if(!isValidEmail(to))
            return ResponseEntity.badRequest().body("Indirizzo email non valido!");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
        return ResponseEntity.status(HttpStatus.CREATED).body("Email inviata con successo all'indirizzo: " + to);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
