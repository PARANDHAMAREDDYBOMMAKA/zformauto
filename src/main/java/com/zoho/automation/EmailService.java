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
        sendFormSubmissionEmail(screenshots, null, formEmail);
    }

    public void sendFormSubmissionEmail(List<File> screenshots, File pdfFile, String formEmail) {
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
            StringBuilder emailBody = new StringBuilder();
            emailBody.append("Form submission completed successfully for: ").append(formEmail).append("\n\n");

            if (pdfFile != null && pdfFile.exists()) {
                emailBody.append("The PDF response is attached to this email.\n\n");
            }

            emailBody.append("Please find attached screenshots of each page.");
            textPart.setText(emailBody.toString());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            // Attach PDF first if available
            if (pdfFile != null && pdfFile.exists()) {
                MimeBodyPart pdfPart = new MimeBodyPart();
                pdfPart.attachFile(pdfFile);
                multipart.addBodyPart(pdfPart);
                System.out.println("Attached PDF: " + pdfFile.getName());
            }

            // Attach screenshots
            for (File screenshot : screenshots) {
                if (screenshot != null && screenshot.exists()) {
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
