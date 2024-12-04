package banquemisr.challenge05.TaskManagementSystem.dto;

import banquemisr.challenge05.TaskManagementSystem.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseDto extends BaseEntity {
    private String id;

    private String title;

    private String description;

    private String status;
    private String priority;
    private LocalDate dueDate;

    private String createdBy;

    private List<String> assignedUsers;
}
