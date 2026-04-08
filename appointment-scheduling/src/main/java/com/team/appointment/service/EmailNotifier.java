package com.team.appointment.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailNotifier implements Notifier {

    private final String fromEmail = "deeda.ishtayeh@gmail.com";
    private final String appPassword = System.getenv("EMAIL_PASS");
    private final String toEmail = "deeda.ishtayeh@gmail.com";

    @Override
    public void send(String message) {

        if (appPassword == null) {
            System.out.println("❌ EMAIL_PASS not set!");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, appPassword);
                    }
                });

        try {
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress(fromEmail));
            emailMessage.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            emailMessage.setSubject("Appointment Notification");
            emailMessage.setText(message);

            Transport.send(emailMessage);

            System.out.println("✅ Email sent successfully!");

        } catch (MessagingException e) {
            System.out.println(" Failed to send email.");
            e.printStackTrace();
        }
    }
}