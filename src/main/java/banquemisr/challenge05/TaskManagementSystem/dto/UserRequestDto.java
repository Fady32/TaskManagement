package banquemisr.challenge05.TaskManagementSystem.dto;

import banquemisr.challenge05.TaskManagementSystem.model.BaseEntity;
import jakarta.validation.constraints.Email;
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
public class UserRequestDto {

    @NotBlank(message = "username cannot be empty")
    private String username;

    @NotBlank(message = "password cannot be empty")
    private String password;

    @NotBlank(message = "email cannot be empty")
    @Email(message = "Invalid email address")
    private String email;

    @NotNull(message = "Invalid roles")
    private Set<@Pattern(regexp = "USER|ADMIN", message = "Roles must be either USER or ADMIN") String> roles;
}
