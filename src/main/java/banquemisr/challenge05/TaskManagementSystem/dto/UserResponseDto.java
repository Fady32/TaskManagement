package banquemisr.challenge05.TaskManagementSystem.dto;

import banquemisr.challenge05.TaskManagementSystem.model.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@Builder
public class UserResponseDto extends BaseEntity {
    private Long id;

    private String username;

    private String email;

    private Set<String> roles;
}
