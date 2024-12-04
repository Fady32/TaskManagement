package banquemisr.challenge05.TaskManagementSystem.service;

import banquemisr.challenge05.TaskManagementSystem.dto.TaskQueryCriteriaRequest;
import banquemisr.challenge05.TaskManagementSystem.dto.TaskRequestDto;
import banquemisr.challenge05.TaskManagementSystem.dto.TaskResponseDto;
import banquemisr.challenge05.TaskManagementSystem.exception.ValidationException;
import banquemisr.challenge05.TaskManagementSystem.exception.UserNotFoundException;
import banquemisr.challenge05.TaskManagementSystem.model.Task;
import banquemisr.challenge05.TaskManagementSystem.model.User;
import banquemisr.challenge05.TaskManagementSystem.repo.TaskRepo;
import banquemisr.challenge05.TaskManagementSystem.repo.TaskSpecification;
import banquemisr.challenge05.TaskManagementSystem.util.TaskConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Service class to handle business logic for Task management.
 */
@Transactional
@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private final UserService userService;
    private final HistoryService historyService;
    private final EmailService emailService;

    /**
     * Create a new Task.
     *
     * @param request The request DTO containing task details.
     * @return TaskResponseDto The response DTO of the created task.
     */
    public TaskResponseDto createTask(TaskRequestDto request) {
        // Find the user to assign the task to
        Set<User> users = userService.findUsersByUsernames(request.getAssignedUsernames());

        if (CollectionUtils.isEmpty(users)) {
            throw new UserNotFoundException("Username not found");
        }

        User loggedInUserName = userService.getCurrentLoggedUser();

        // Convert the TaskRequestDto to Task entity
        Task task = TaskConverter.createNewEntity(request, users, loggedInUserName.getUsername());

        // Save the task entity to the database
        Task savedTask = taskRepo.save(task);

        // Create history entry for the task creation
        historyService.createHistory(savedTask, null, loggedInUserName, "Creation");

        // Send notification in new thread
        CompletableFuture.runAsync(() -> {
            task.getUsers().forEach(user -> emailService.sendTaskAssignedEmail(user.getEmail(), task.getTitle()));
        });

        // Return the task response DTO
        return TaskConverter.toResponse(savedTask);
    }

    /**
     * Update an existing Task.
     *
     * @param id             The ID of the task to update.
     * @param taskRequestDto The updated task data.
     * @return TaskResponseDto The response DTO of the updated task.
     * @throws RuntimeException If task with given ID does not exist.
     */
    public TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto) {
        // Fetch the existing task by ID
        Optional<Task> optionalTask = taskRepo.findById(id);

        if (optionalTask.isPresent()) {
            // Convert the updated request DTO to Task entity
            Task task = TaskConverter.updateTaskEntity(taskRequestDto, Collections.emptySet(), optionalTask.get());

            // Save the updated task to the database
            Task updatedTask = taskRepo.save(task);

            // Create history entry for the updated task
            User loggedInUserName = userService.getCurrentLoggedUser();
            historyService.createHistory(task, updatedTask, loggedInUserName, "Modification");

            // Send notification in new thread
            CompletableFuture.runAsync(() -> {
                task.getUsers().forEach(user -> emailService.sendTaskUpdatedEmail(user.getEmail(), task.getTitle()));
            });

            // Return the updated task as a response DTO
            return TaskConverter.toResponse(task);
        }

        // Throw exception if task is not found
        throw new RuntimeException("Task not found");
    }

    /**
     * Get all tasks with pagination and filtering.
     *
     * @param queryRequest The request containing filter criteria.
     * @return Page<TaskResponseDto> A page of task response DTOs with applied filters.
     */
    public Page<TaskResponseDto> queryTasks(TaskQueryCriteriaRequest queryRequest) {

        // Create a Pageable object using the request parameters
        Pageable pageable = PageRequest.of(queryRequest.getPage(), queryRequest.getSize());

        validatePagination(pageable);

        // Build the specification for filtering tasks based on query parameters
        Specification<Task> spec = TaskSpecification.buildTaskSpecification(queryRequest);

        // Retrieve tasks from the repository with the specification and pageable
        Page<Task> tasksPage = taskRepo.findAll(spec, pageable);


        tasksPage.stream().map(task -> TaskConverter.toResponse(task));
        // Convert tasks to TaskResponseDto (DTOs) for the API response
        return tasksPage.map(TaskConverter::toResponse);
    }


    /**
     * Validates the sorting parameters.
     *
     * @throws IllegalArgumentException if validation fails
     */
    private void validatePagination(Pageable pageable) {
        if (pageable.getPageSize() <= 0 || pageable.getPageSize() > 100 || pageable.getPageNumber() < 0) {
            throw new ValidationException("Page size must be from 1 to 100");
        }
    }

    /**
     * Get a task by its ID.
     *
     * @param id The ID of the task to fetch.
     * @return TaskResponseDto The task response DTO.
     * @throws RuntimeException If task with given ID does not exist.
     */
    public TaskResponseDto getTaskById(Long id) {
        // Find task by ID and throw exception if not found
        Optional<Task> task = taskRepo.findById(id);
        return task.map(TaskConverter::toResponse).orElseThrow(() -> new ValidationException("Task not found"));
    }

    /**
     * Delete a task by its ID.
     *
     * @param id The ID of the task to delete.
     * @throws RuntimeException If task with given ID does not exist.
     */
    public void deleteTask(Long id) {
        // Verify the task exists before deleting
        Task task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        // Delete the task from the repository
        taskRepo.deleteById(id);

        // Create history entry for the updated task
        User loggedInUserName = userService.getCurrentLoggedUser();
        historyService.createHistory(task, null, loggedInUserName, "Deletion");
    }
}
