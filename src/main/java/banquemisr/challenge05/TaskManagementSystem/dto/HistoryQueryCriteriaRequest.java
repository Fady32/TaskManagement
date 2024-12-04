package banquemisr.challenge05.TaskManagementSystem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryQueryCriteriaRequest {

    @Pattern(regexp = "Creation|Modification|Deletion", message = "Action must be one of the following: Creation, Modification, Deletion.")
    private String action = null;
    private Long taskId = null;
    private Long userId = null;
    private String username = null;

    @Min(value = 0, message = "Page must be at least 0.")
    private int page;
    @Min(value = 1, message = "Size must be between 1 and 100.")
    @Max(value = 100, message = "Size must be between 1 and 100.")
    private int size;
}
