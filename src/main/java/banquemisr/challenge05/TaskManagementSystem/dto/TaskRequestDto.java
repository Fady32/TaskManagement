package banquemisr.challenge05.TaskManagementSystem.dto;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TaskRequestDto {

    @NotBlank(message = "Title cannot be empty. Please provide a valid task title.")
    private String title;

    private String description;

    @NotNull(message = "Status is required. Please select a valid task status.")
    @Pattern(regexp = "^(TODO|IN_PROGRESS|DONE)$", message = "Invalid status. Available statuses: TODO, IN_PROGRESS, DONE. Please choose one.")
    private String status;

    @NotNull(message = "Priority is required. Please select a valid priority level.")
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH)$", message = "Invalid priority. Available priorities: LOW, MEDIUM, HIGH. Please choose one.")
    private String priority;

    @NotNull(message = "Due date cannot be null. Please specify a due date for the task.")
    @FutureOrPresent(message = "Due date must be a present or date. Please provide a valid date.")
    private LocalDate dueDate;

    @NotEmpty(message = "At least one user must be assigned to the task. Please provide valid usernames.")
    private Set<String> assignedUsernames;

}
