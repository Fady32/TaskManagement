package banquemisr.challenge05.TaskManagementSystem.controller;

import banquemisr.challenge05.TaskManagementSystem.dto.ResponseModel;
import banquemisr.challenge05.TaskManagementSystem.dto.TaskQueryCriteriaRequest;
import banquemisr.challenge05.TaskManagementSystem.dto.TaskRequestDto;
import banquemisr.challenge05.TaskManagementSystem.dto.TaskResponseDto;
import banquemisr.challenge05.TaskManagementSystem.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Controller for managing tasks, including creating, updating, retrieving, and deleting tasks.
 * Supports pagination and filtering with HATEOAS links for improved API discoverability.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    private final TaskService taskService; // Service to handle task-related logic

    /**
     * Creates a new task and returns the created task.
     *
     * @param taskRequest The task request data to create the task.
     * @return A ResponseEntity containing the created TaskResponseDto and a CREATED status.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel<TaskResponseDto>> createTask(@Valid @RequestBody TaskRequestDto taskRequest) {
        TaskResponseDto createdTask = taskService.createTask(taskRequest);
        // Return single task wrapped in GenericApiResponse
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseModel<>(createdTask, null));
    }

    /**
     * Updates an existing task by its ID.
     *
     * @param id          The ID of the task to be updated.
     * @param taskRequest The updated task data.
     * @return A ResponseEntity containing the updated TaskResponseDto if successful, otherwise null.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel<TaskResponseDto>> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDto taskRequest) {
        TaskResponseDto updatedTask = taskService.updateTask(id, taskRequest);

        if (updatedTask != null) {
            // Return updated task wrapped in GenericApiResponse
            return ResponseEntity.ok(new ResponseModel<>(updatedTask, null));
        }

        // Returning 404 if task not found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    /**
     * Retrieves a paginated list of tasks based on the provided query criteria (filtering) and pagination.
     *
     * @return A GenericApiResponse containing the paginated list of tasks, pagination metadata, and HATEOAS links.
     */
    @PostMapping("/query")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<ResponseModel<List<TaskResponseDto>>> queryTasks(@RequestBody TaskQueryCriteriaRequest criteriaRequest) {

        // Retrieve paginated and filtered tasks from the service
        Page<TaskResponseDto> taskPage = taskService.queryTasks(criteriaRequest);

        // Extract the list of tasks from the page
        List<TaskResponseDto> taskModels = taskPage.getContent();

        // Pagination metadata
        ResponseModel.PaginationMetadata paginationMetadata = new ResponseModel.PaginationMetadata(
                taskPage.getSize(),
                taskPage.getTotalElements(),
                taskPage.getTotalPages(),
                taskPage.getNumber()
        );

        // Create and wrap the data into the ResponseModel
        ResponseModel<List<TaskResponseDto>> response = new ResponseModel<>(taskModels, paginationMetadata);

        // Return the response wrapped in a ResponseEntity
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to be retrieved.
     * @return A ResponseEntity containing the TaskResponseDto for the specified task.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel<TaskResponseDto>> getTaskById(@PathVariable Long id) {
        TaskResponseDto task = taskService.getTaskById(id);

        if (Objects.isNull(task)) {
            return ResponseEntity.badRequest().body(null);
        }
        // Wrap the task in GenericApiResponse for consistency
        return ResponseEntity.ok(new ResponseModel<>(task, null));
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to be deleted.
     * @return A ResponseEntity indicating the status of the deletion (ACCEPTED for successful deletion).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
