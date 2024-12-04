package banquemisr.challenge05.TaskManagementSystem.cron;

import banquemisr.challenge05.TaskManagementSystem.model.Notification;
import banquemisr.challenge05.TaskManagementSystem.model.Task;
import banquemisr.challenge05.TaskManagementSystem.model.User;
import banquemisr.challenge05.TaskManagementSystem.repo.TaskRepo;
import banquemisr.challenge05.TaskManagementSystem.repo.TaskSpecification;
import banquemisr.challenge05.TaskManagementSystem.service.EmailService;
import banquemisr.challenge05.TaskManagementSystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class NotificationCronJob {

    private final TaskRepo taskRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;

    // Inject the threshold value (e.g., 24 hours) from application.properties
    @Value("${task.notification.threshold.hours}")
    private int notificationThresholdHours;

    // Schedule the job to run once a day (every 24 hours)
    @Scheduled(cron = "0 0 0 * * ?")  // This cron expression runs the job at midnight every day
    //@Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    public void checkAndNotifyTaskDeadline() {
        LocalDate currentTime = LocalDate.now();

        // Retrieve all tasks from the repository
        Specification<Task> tasksCriteria = TaskSpecification.buildTaskSpecificationForNotification(notificationThresholdHours);
        Iterable<Task> tasks = taskRepository.findAll(tasksCriteria);

        tasks.forEach(task -> {
            // Skip tasks with no due date or those whose due date has already passed
            if (task.getDueDate() == null || task.getDueDate().isBefore(currentTime)) {
                return;
            }

            // Calculate the time remaining until the task's due date
            long hoursUntilDeadline = Duration.between(currentTime.atStartOfDay(), task.getDueDate().atStartOfDay()).toHours();

            // If the remaining time is less than or equal to the threshold, send notifications
            if (hoursUntilDeadline <= notificationThresholdHours) {
                String taskTitle = task.getTitle();
                String dueDate = task.getDueDate().toString();

                // Send email notifications to each user associated with the task
                task.getUsers().forEach(user -> {
                    boolean mailSent = emailService.sendTaskDeadlineEmail(user.getEmail(), taskTitle, dueDate);
                    this.buildNotification(taskTitle, user, mailSent);
                });
            }
        });
    }


    private void buildNotification(String message, User user, boolean isSent) {
        Notification notification = Notification.builder().message(message).user(user).isSent(isSent).build();
        notificationService.saveNotification(notification);
    }
}
