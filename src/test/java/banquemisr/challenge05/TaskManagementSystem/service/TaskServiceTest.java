package banquemisr.challenge05.TaskManagementSystem.service;

import banquemisr.challenge05.TaskManagementSystem.dto.TaskQueryCriteriaRequest;
import banquemisr.challenge05.TaskManagementSystem.dto.TaskRequestDto;
import banquemisr.challenge05.TaskManagementSystem.dto.TaskResponseDto;
import banquemisr.challenge05.TaskManagementSystem.exception.UserNotFoundException;
import banquemisr.challenge05.TaskManagementSystem.model.Task;
import banquemisr.challenge05.TaskManagementSystem.model.User;
import banquemisr.challenge05.TaskManagementSystem.repo.TaskRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    @Mock
    private TaskRepo taskRepo;
    @Mock
    private UserService userService;
    @Mock
    private HistoryService historyService;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private TaskService taskService;

    private TaskRequestDto taskRequestDto;
    private Task task;
    User user = new User();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        user.setUsername("user1");

        taskRequestDto = TaskRequestDto.builder()
                .title("Test Task")
                .description("Task description")
                .status("IN_PROGRESS")
                .priority("HIGH")
                .dueDate(LocalDate.now())
                .assignedUsernames(Set.of("user1"))
                .build();

        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Task description")
                .status("IN_PROGRESS")
                .priority("HIGH")
                .dueDate(LocalDate.now())
                .createdBy("admin")
                .users(new HashSet<>(List.of(user)))
                .build();

    }

    @Test
    void testCreateTask_success() {
        Mockito.when(userService.findUsersByUsernames(Mockito.any())).thenReturn(new HashSet<>(Collections.singletonList(user)));
        Mockito.when(taskRepo.save(Mockito.any(Task.class))).thenReturn(task);
        Mockito.when(userService.getCurrentLoggedUser()).thenReturn(user);


        TaskResponseDto result = taskService.createTask(taskRequestDto);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        Mockito.verify(taskRepo, Mockito.times(1)).save(Mockito.any(Task.class));
    }

    @Test
    void testCreateTask_userNotFound() {
        Mockito.when(userService.findUsersByUsernames(Mockito.any())).thenReturn(Collections.emptySet());

        assertThrows(UserNotFoundException.class, () -> taskService.createTask(taskRequestDto));
    }

    @Test
    void testUpdateTask_success() {
        Mockito.when(taskRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(task));
        Mockito.when(taskRepo.save(Mockito.any(Task.class))).thenReturn(task);

        TaskResponseDto result = taskService.updateTask(1L, taskRequestDto);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        Mockito.verify(taskRepo, Mockito.times(1)).save(Mockito.any(Task.class));
    }

    @Test
    void testUpdateTask_taskNotFound() {
        Mockito.when(taskRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.updateTask(1L, taskRequestDto));
    }

    @Test
    void testQueryTasks_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(List.of(task), pageable, 1);
        Mockito.when(taskRepo.findAll(Mockito.any(Specification.class), Mockito.eq(pageable))).thenReturn(taskPage);

        Page<TaskResponseDto> result = taskService.queryTasks(new TaskQueryCriteriaRequest(null, null,
                null, null, 0, 10, null));

        assertNotNull(result);
        Mockito.verify(taskRepo, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    void testGetTaskById_success() {
        Mockito.when(taskRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(task));

        TaskResponseDto result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
    }

    @Test
    void testGetTaskById_taskNotFound() {
        Mockito.when(taskRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void testDeleteTask_success() {
        Mockito.when(taskRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        Mockito.verify(taskRepo, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void testDeleteTask_taskNotFound() {
        Mockito.when(taskRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.deleteTask(1L));
    }
}
