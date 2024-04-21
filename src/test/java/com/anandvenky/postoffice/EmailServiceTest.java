package com.anandvenky.postoffice;

import com.anandvenky.postoffice.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;

import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmailWithAttachment() throws MessagingException {
        // Given
        String toEmail = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Hello, this is a test email!";
        String attachmentPath = "/path/to/attachment.txt";

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        doNothing().when(javaMailSender).send(mimeMessage);

        // When
        emailService.sendEmailWithAttachment(toEmail, subject, body, attachmentPath);

        // Then
        verify(javaMailSender, times(1)).send(mimeMessage);
        verify(helper, times(1)).setTo(toEmail);
        verify(helper, times(1)).setSubject(subject);
        verify(helper, times(1)).setText(body);

        File file = new File(attachmentPath);
        verify(helper, times(1)).addAttachment(file.getName(), file);
    }
}
