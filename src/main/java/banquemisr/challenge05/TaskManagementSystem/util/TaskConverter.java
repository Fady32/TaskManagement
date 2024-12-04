package banquemisr.challenge05.TaskManagementSystem.util;

import banquemisr.challenge05.TaskManagementSystem.dto.TaskRequestDto;
import banquemisr.challenge05.TaskManagementSystem.dto.TaskResponseDto;
import banquemisr.challenge05.TaskManagementSystem.model.Task;
import banquemisr.challenge05.TaskManagementSystem.model.TaskPriority;
import banquemisr.challenge05.TaskManagementSystem.model.TaskStatus;
import banquemisr.challenge05.TaskManagementSystem.model.User;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public class TaskConverter {

    public static Task createNewEntity(TaskRequestDto taskRequest, Set<User> users, String currentUser) {

        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(taskRequest.getStatus())
                .priority(taskRequest.getPriority())
                .dueDate(taskRequest.getDueDate())
                .createdBy(currentUser)
                .build();

        if (!CollectionUtils.isEmpty(users)) {
            task.setUsers(users);
        }

        return task;

    }


    public static Task updateTaskEntity(TaskRequestDto updatedTaskDto, Set<User> users, Task existingTask) {
        if (updatedTaskDto.getTitle() != null) {
            existingTask.setTitle(updatedTaskDto.getTitle());
        }
        if (updatedTaskDto.getDescription() != null) {
            existingTask.setDescription(updatedTaskDto.getDescription());
        }
        if (updatedTaskDto.getStatus() != null) {
            existingTask.setStatus(updatedTaskDto.getStatus());
        }
        if (updatedTaskDto.getPriority() != null) {
            existingTask.setPriority(updatedTaskDto.getPriority());
        }
        if (updatedTaskDto.getDueDate() != null) {
            existingTask.setDueDate(updatedTaskDto.getDueDate());
        }

        if (users != null && !users.isEmpty()) {
            existingTask.setUsers(users);
        }

        return existingTask;

    }

    public static TaskResponseDto toResponse(Task task) {

        TaskResponseDto response = TaskResponseDto.builder().title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus()).priority(task.getPriority())
                .id(String.valueOf(task.getId()))
                .createdBy(task.getCreatedBy())
                .assignedUsers(task.getUsers().stream().map(User::getUsername).collect(Collectors.toList()))
                .dueDate(task.getDueDate()).build();
        response.setCreatedDate(task.getCreatedDate());
        response.setUpdatedDate(task.getUpdatedDate())
        ;
        return response;
    }

}



