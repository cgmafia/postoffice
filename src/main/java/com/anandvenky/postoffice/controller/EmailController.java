package com.anandvenky.postoffice.controller;

import com.anandvenky.postoffice.service.EmailReceiveService;
import com.anandvenky.postoffice.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;
    private final EmailReceiveService emailReceiveService;

    public EmailController(EmailService emailService, EmailReceiveService emailReceiveService) {
        this.emailService = emailService;
        this.emailReceiveService = emailReceiveService;
    }

    @PostMapping("/send")
    public void sendEmailWithAttachment(@RequestParam String toEmail,
                                        @RequestParam String subject,
                                        @RequestParam String body,
                                        @RequestParam String attachmentPath) throws MessagingException {
        emailService.sendEmailWithAttachment(toEmail, subject, body, attachmentPath);
    }

    @GetMapping("/receive")
    public void receiveEmailsWithAttachments() throws MessagingException, IOException {
        emailReceiveService.receiveEmailsWithAttachments();
    }
}
