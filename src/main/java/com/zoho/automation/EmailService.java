package com.zoho.automation;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.List;
import java.util.Properties;

public class EmailService {
    private String smtpHost;
    private String smtpPort;
    private String smtpUsername;
    private String smtpPassword;
    private String notificationEmail;

    public EmailService(Properties config) {
        this.smtpHost = config.getProperty("smtp.host");
        this.smtpPort = config.getProperty("smtp.port");
        this.smtpUsername = config.getProperty("smtp.username");
        this.smtpPassword = config.getProperty("smtp.password");
        this.notificationEmail = config.getProperty("notification.email");
    }

    public void sendScreenshots(List<File> screenshots, String formEmail) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notificationEmail));
            message.setSubject("Zoho Form Submitted - " + formEmail);

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Form submission completed successfully for: " + formEmail + "\n\nPlease find attached screenshots of each page.");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            for (File screenshot : screenshots) {
                if (screenshot.exists()) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(screenshot);
                    multipart.addBodyPart(attachmentPart);
                }
            }

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Email sent successfully to: " + notificationEmail);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
