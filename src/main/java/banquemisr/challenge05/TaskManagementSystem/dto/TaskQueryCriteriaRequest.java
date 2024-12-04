package banquemisr.challenge05.TaskManagementSystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class TaskQueryCriteriaRequest {

    @Size(max = 255, message = "Title must not exceed 255 characters.")
    private String title = null;

    @Size(max = 500, message = "Description must not exceed 500 characters.")
    private String description = null;

    @Pattern(regexp = "TODO|IN_PROGRESS|DONE", message = "Status must be one of the following: TODO, IN_PROGRESS, DONE.")
    private String status = null;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Due date cannot be in the past.")
    private LocalDate dueDate = null;

    @Min(value = 0, message = "Page must be at least 0.")
    private int page;

    @Min(value = 1, message = "Size must be between 1 and 100.")
    @Max(value = 100, message = "Size must be between 1 and 100.")
    private int size;

    @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "Priority must be one of the following: LOW, MEDIUM, HIGH.")
    private String priority = null;
}
