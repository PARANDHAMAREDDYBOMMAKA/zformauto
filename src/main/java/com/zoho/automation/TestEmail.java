package com.zoho.automation;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TestEmail {
    public static void main(String[] args) {
        try {
            Properties config = new Properties();
            FileInputStream fis = new FileInputStream("config.properties");
            config.load(fis);
            fis.close();

            List<File> testFiles = new ArrayList<>();

            File screenshotDir = new File("screenshots");
            if (screenshotDir.exists() && screenshotDir.isDirectory()) {
                File[] files = screenshotDir.listFiles((dir, name) -> name.endsWith(".png"));
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        testFiles.add(file);
                    }
                    System.out.println("Found " + testFiles.size() + " screenshots to send");
                } else {
                    System.out.println("No screenshots found. Creating a test note...");
                }
            } else {
                System.out.println("Screenshots directory not found. Email will be sent without attachments.");
            }

            System.out.println("Sending test email...");
            EmailService emailService = new EmailService(config);
            emailService.sendScreenshots(testFiles, config.getProperty("email"));
            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
