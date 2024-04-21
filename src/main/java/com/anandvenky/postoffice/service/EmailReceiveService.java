package com.anandvenky.postoffice.service;

import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
public class EmailReceiveService {
    private final String username;
    private final String password;

    public EmailReceiveService(@Value("${spring.mail.username}") String username,
                               @Value("${spring.mail.password}") String password) {
        this.username = username;
        this.password = password;
    }

    public void receiveEmailsWithAttachments() throws MessagingException, IOException {
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imaps");

        Session session = Session.getInstance(properties);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", username, password);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();
        for (Message message : messages) {
            if (message instanceof MimeMessage) {
                MimeMessage mimeMessage = (MimeMessage) message;
                Object content = mimeMessage.getContent();
                if (content instanceof Multipart) {
                    Multipart multipart = (Multipart) content;
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);
                        if (bodyPart instanceof MimeBodyPart) {
                            MimeBodyPart mimeBodyPart = (MimeBodyPart) bodyPart;
                            if (Part.ATTACHMENT.equalsIgnoreCase(mimeBodyPart.getDisposition())) {
                                mimeBodyPart.saveFile("/path/to/save/" + mimeBodyPart.getFileName());
                            }
                        }
                    }
                }
            }
        }

        inbox.close(false);
        store.close();
    }
}
