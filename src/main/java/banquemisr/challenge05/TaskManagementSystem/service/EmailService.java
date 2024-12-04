package banquemisr.challenge05.TaskManagementSystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender javaMailSender;

    // Admin email to notify in case of failures
    @Value("${admin.email}")
    private String ADMIN_EMAIL = "dorcas.toy81@ethereal.email";

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean sendTaskDeadlineEmail(String recipientEmail, String taskTitle, String dueDate) {
        SimpleMailMessage message = createEmailMessage(recipientEmail, "Task Deadline Notification: " + taskTitle,
                "Dear User,\n\nThis is a reminder that the task '" + taskTitle + "' is due on " + dueDate + ".\n\nPlease take the necessary action.");
        return sendEmail(message);
    }

    public boolean sendTaskUpdatedEmail(String recipientEmail, String taskTitle) {
        SimpleMailMessage message = createEmailMessage(recipientEmail, "Task Update: " + taskTitle,
                "Dear User,\n\nThe task '" + taskTitle + "' has been updated.\n\nBest regards, Task Management System");
        return sendEmail(message);
    }

    public boolean sendTaskAssignedEmail(String recipientEmail, String taskTitle) {
        SimpleMailMessage message = createEmailMessage(recipientEmail, "Task Assigned: " + taskTitle,
                "Dear User,\n\nThe task '" + taskTitle + "' has been assigned to you.\n\nBest regards, Task Management System");
        return sendEmail(message);
    }

    // Helper method to create SimpleMailMessage object
    private SimpleMailMessage createEmailMessage(String recipientEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

    // General email sending method with exception handling
    private boolean sendEmail(SimpleMailMessage message) {
        try {
            javaMailSender.send(message);
            logger.info("Email successfully sent to: {}", (Object) message.getTo());
            return true;
        } catch (MailParseException | MailAuthenticationException | MailSendException e) {
            handleEmailException(e, message);
        } catch (Exception e) {
            logger.error("Unexpected error while sending email: {}", e.getMessage(), e);
            notifyAdminOfFailure(e.getMessage());
        }

        return false;
    }

    // Handle specific email sending exceptions
    private void handleEmailException(Exception e, SimpleMailMessage message) {
        if (e instanceof MailParseException) {
            logger.error("Mail parsing error while sending email to: {}", message.getTo(), e);
        } else if (e instanceof MailAuthenticationException) {
            logger.error("Authentication error while sending email to: {}", message.getTo(), e);
        } else if (e instanceof MailSendException) {
            logger.error("Error sending email to: {}", message.getTo(), e);
        }
        // Notify admin for email failure
        notifyAdminOfFailure(e.getMessage());
    }

    // Send a notification email to admin in case of failure
    private void notifyAdminOfFailure(String errorMessage) {
        SimpleMailMessage adminMessage = new SimpleMailMessage();
        adminMessage.setTo(ADMIN_EMAIL);
        adminMessage.setSubject("Email Sending Failure Notification");
        adminMessage.setText("Dear Admin,\n\nAn error occurred while sending an email:\n" + errorMessage + "\n\nBest regards, Task Management System");

        try {
            javaMailSender.send(adminMessage);
            logger.warn("Admin notified of email failure.");
        } catch (Exception ex) {
            logger.error("Failed to send email notification to admin.", ex);
        }
    }
}
