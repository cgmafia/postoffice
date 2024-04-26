package com.anandvenky.postoffice.controller;

import com.anandvenky.postoffice.service.EmailReceiveService;
import com.anandvenky.postoffice.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class EmailController {

    private final EmailService emailService;
    private final EmailReceiveService emailReceiveService;

    private static final String UPLOAD_DIR = "C://uploads/";

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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload", HttpStatus.BAD_REQUEST);
        }

        try {
            // Get the filename
            String fileName = file.getOriginalFilename();

            // Create the directory if it doesn't exist
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File destFile = new File(UPLOAD_DIR + fileName);
            file.transferTo(destFile);


            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
