package banquemisr.challenge05.TaskManagementSystem.repo;


import banquemisr.challenge05.TaskManagementSystem.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {
}