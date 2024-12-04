package banquemisr.challenge05.TaskManagementSystem.cron;

import banquemisr.challenge05.TaskManagementSystem.model.Notification;
import banquemisr.challenge05.TaskManagementSystem.model.Task;
import banquemisr.challenge05.TaskManagementSystem.model.User;
import banquemisr.challenge05.TaskManagementSystem.repo.TaskRepo;
import banquemisr.challenge05.TaskManagementSystem.service.EmailService;
import banquemisr.challenge05.TaskManagementSystem.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

class NotificationCronJobTest {

    @Mock
    private TaskRepo taskRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationCronJob notificationCronJob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testNoTasksFound() {
        // Given
        when(taskRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        // When
        notificationCronJob.checkAndNotifyTaskDeadline();

        // Then
        verify(taskRepository, times(1)).findAll(any(Specification.class));
        verifyNoInteractions(emailService, notificationService);
    }

    @Test
    void testTaskWithoutDueDate() {
        // Given
        Task taskWithoutDueDate = Task.builder().title("Task 1").users(Set.of()).build();
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskWithoutDueDate));

        // When
        notificationCronJob.checkAndNotifyTaskDeadline();

        // Then
        verify(taskRepository, times(1)).findAll(any(Specification.class));
        verifyNoInteractions(emailService, notificationService);
    }

    @Test
    void testTaskWithPastDueDate() {
        // Given
        Task taskWithPastDueDate = Task.builder().title("Task 1").dueDate(LocalDate.now().minusDays(1)).users(Set.of()).build();
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskWithPastDueDate));

        // When
        notificationCronJob.checkAndNotifyTaskDeadline();

        // Then
        verify(taskRepository, times(1)).findAll(any(Specification.class));
        verifyNoInteractions(emailService, notificationService);
    }

    @Test
    void testTaskWithDueDateWithinThreshold() {
        // Given
        User user = User.builder().email("test@example.com").build();
        Task taskWithinThreshold = Task.builder()
                .title("Task 1")
                .dueDate(LocalDate.now())
                .users(Set.of(user))
                .build();
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskWithinThreshold));
        when(emailService.sendTaskDeadlineEmail(user.getEmail(), "Task 1", taskWithinThreshold.getDueDate().toString()))
                .thenReturn(true);

        // When
        notificationCronJob.checkAndNotifyTaskDeadline();

        // Then
        verify(taskRepository, times(1)).findAll(any(Specification.class));
        verify(emailService, times(1)).sendTaskDeadlineEmail(user.getEmail(), "Task 1", taskWithinThreshold.getDueDate().toString());
        verify(notificationService, times(1)).saveNotification(any(Notification.class));
    }

    @Test
    void testTaskWithDueDateBeyondThreshold() {
        // Given
        Task taskBeyondThreshold = Task.builder()
                .title("Task 1")
                .dueDate(LocalDate.now().plusDays(2))
                .users(Set.of())
                .build();
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskBeyondThreshold));

        // When
        notificationCronJob.checkAndNotifyTaskDeadline();

        // Then
        verify(taskRepository, times(1)).findAll(any(Specification.class));
        verifyNoInteractions(emailService, notificationService);
    }

    @Test
    void testMultipleTasksWithMixedScenarios() {
        // Given
        User user1 = User.builder().email("user1@example.com").build();
        User user2 = User.builder().email("user2@example.com").build();

        Task taskWithoutDueDate = Task.builder().title("No Due Date Task").users(Set.of(user1)).build();
        Task taskWithPastDueDate = Task.builder().title("Past Due Date Task").dueDate(LocalDate.now().minusDays(1)).users(Set.of(user2)).build();
        Task taskWithinThreshold = Task.builder().title("Within Threshold Task").dueDate(LocalDate.now()).users(Set.of(user1)).build();

        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskWithoutDueDate, taskWithPastDueDate, taskWithinThreshold));
        when(emailService.sendTaskDeadlineEmail(user1.getEmail(), "Within Threshold Task", taskWithinThreshold.getDueDate().toString()))
                .thenReturn(true);

        // When
        notificationCronJob.checkAndNotifyTaskDeadline();

        // Then
        verify(taskRepository, times(1)).findAll(any(Specification.class));
        verify(emailService, times(1)).sendTaskDeadlineEmail(user1.getEmail(), "Within Threshold Task", taskWithinThreshold.getDueDate().toString());
        verify(notificationService, times(1)).saveNotification(any(Notification.class));
    }
}
