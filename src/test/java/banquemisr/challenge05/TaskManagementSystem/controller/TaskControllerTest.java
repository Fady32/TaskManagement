package banquemisr.challenge05.TaskManagementSystem.controller;

import banquemisr.challenge05.TaskManagementSystem.dto.TaskRequestDto;
import banquemisr.challenge05.TaskManagementSystem.dto.TaskResponseDto;
import banquemisr.challenge05.TaskManagementSystem.model.User;
import banquemisr.challenge05.TaskManagementSystem.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;


    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;
    private TaskRequestDto taskRequestDto;
    private TaskResponseDto taskResponseDto;
    private User user = new User();
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Explicitly register JavaTimeModule in ObjectMapper for test
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
        user.setUsername("user1");
        taskRequestDto = TaskRequestDto.builder()
                .title("Test Task")
                .description("Task description")
                .status("IN_PROGRESS")
                .priority("HIGH")
                .dueDate(LocalDate.now())
                .assignedUsernames(Set.of("user1"))
                .build();

        taskResponseDto = TaskResponseDto.builder()
                .id("1")
                .title("Test Task")
                .description("Task description")
                .status("IN_PROGRESS")
                .priority("HIGH")
                .assignedUsers(List.of("user1"))
                .createdBy("admin")
                .build();
    }

    @Test
    void testCreateTask_success() throws Exception {
        when(taskService.createTask(any(TaskRequestDto.class))).thenReturn(taskResponseDto);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("Test Task"))
                .andExpect(jsonPath("$.data.id").value("1"));
    }


    @Test
    void testGetTaskById_success() throws Exception {
        when(taskService.getTaskById(anyLong())).thenReturn(taskResponseDto);

        mockMvc.perform(get("/api/v1/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void testGetTaskById_taskNotFound() throws Exception {
        when(taskService.getTaskById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/v1/tasks/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }
}
