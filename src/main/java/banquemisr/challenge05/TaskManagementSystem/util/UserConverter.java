package banquemisr.challenge05.TaskManagementSystem.util;

import banquemisr.challenge05.TaskManagementSystem.dto.UserRequestDto;
import banquemisr.challenge05.TaskManagementSystem.dto.UserResponseDto;
import banquemisr.challenge05.TaskManagementSystem.model.User;

public class UserConverter {

    public static User toEntity(UserRequestDto userDto) {

        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .roles(userDto.getRoles())
                .email(userDto.getEmail())
                .build();

    }

    public static UserResponseDto toResponse(User user) {

        UserResponseDto response  = UserResponseDto.builder().username(user.getUsername())
                .id(user.getId())
                .roles(user.getRoles()).email(user.getEmail()).build();
        response.setCreatedDate(user.getCreatedDate());
        response.setUpdatedDate(user.getUpdatedDate());
        return response;
    }

}



