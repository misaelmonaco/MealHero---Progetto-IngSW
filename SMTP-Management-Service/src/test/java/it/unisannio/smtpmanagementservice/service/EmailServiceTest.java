package it.unisannio.smtpmanagementservice.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendSimpleEmail_Success() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Body";

        ResponseEntity response = emailService.sendSimpleEmail(to, subject, text);

        assertEquals(201, response.getStatusCodeValue());
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendSimpleEmail_InvalidEmail() {
        String to = "invalid-email";
        String subject = "Test Subject";
        String text = "Test Body";

        ResponseEntity response = emailService.sendSimpleEmail(to, subject, text);

        assertEquals(400, response.getStatusCodeValue());
        verify(emailSender, never()).send(any(SimpleMailMessage.class));
    }
}
