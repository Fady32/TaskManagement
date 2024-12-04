package banquemisr.challenge05.TaskManagementSystem.service;

import banquemisr.challenge05.TaskManagementSystem.model.Notification;
import banquemisr.challenge05.TaskManagementSystem.repo.NotificationRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepository;

    public NotificationService(NotificationRepo notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
}
