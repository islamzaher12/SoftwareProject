package com.team.appointment.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

/**
 * A {@link Notifier} implementation that delivers notification messages
 * via Gmail SMTP.  Requires the environment variable {@code EMAIL_PASS}
 * to be set to the Gmail App Password for the sender account.
 *
 * <p>If {@code EMAIL_PASS} is not set the method logs a warning to the
 * console and returns without throwing.
 *
 * @author Team
 * @version 1.0
 */
public class EmailNotifier implements Notifier {

    /** Gmail address used as both sender and recipient for demo purposes. */
    private final String fromEmail = "s12240028@stu.najah.edu";

    /** Gmail App Password read from the {@code EMAIL_PASS} environment variable. */
    private final String appPassword = System.getenv("EMAIL_PASS");

    /** Recipient email address. */
    private final String toEmail = "s12240028@stu.najah.edu";

    /**
     * Sends the given message as an email via Gmail SMTP.
     * Silently skips if {@code EMAIL_PASS} is not configured.
     *
     * @param message the notification body text
     */
    @Override
    public void send(String message) {

        if (appPassword == null) {
            System.out.println("[EmailNotifier] EMAIL_PASS not set – skipping email send.");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
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
            System.out.println("[EmailNotifier] Email sent successfully.");

        } catch (MessagingException e) {
            System.out.println("[EmailNotifier] Failed to send email: " + e.getMessage());
        }
    }
}
