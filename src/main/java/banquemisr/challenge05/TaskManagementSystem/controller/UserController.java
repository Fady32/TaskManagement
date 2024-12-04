package banquemisr.challenge05.TaskManagementSystem.controller;

import banquemisr.challenge05.TaskManagementSystem.dto.*;
import banquemisr.challenge05.TaskManagementSystem.service.AuthService;
import banquemisr.challenge05.TaskManagementSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing user operations, including registration, authentication,
 * user retrieval, and deletion. It supports role-based access control (RBAC)
 * for securing certain endpoints.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService; // Service to handle user-related logic

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel<List<UserResponseDto>>> getAllUsers(Pageable pageable) {
        // Retrieve the paginated list of users from the service
        Page<UserResponseDto> userPage = userService.getAllUsers(pageable);

        // Create pagination metadata
        ResponseModel.PaginationMetadata paginationMetadata = new ResponseModel.PaginationMetadata(
                userPage.getSize(), userPage.getTotalElements(), userPage.getTotalPages(), userPage.getNumber());

        // Wrap the list of users in the ResponseModel with pagination metadata and links
        ResponseModel<List<UserResponseDto>> response = new ResponseModel<>(userPage.getContent(), paginationMetadata);

        // Return the response wrapped in a ResponseEntity
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a specific user by their ID (accessible by ADMIN or the logged-in user).
     *
     * @param username The Username of the user to retrieve.
     * @return A ResponseEntity containing the requested user wrapped in GenericApiResponse.
     */
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') || #username == principal.username")
    public ResponseEntity<ResponseModel<UserResponseDto>> getUserByUsername(@PathVariable String username) {
        UserResponseDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(new ResponseModel<>(user, null));
    }

    /**
     * Deletes a user by their ID (ADMIN role only).
     *
     * @param id The ID of the user to delete.
     * @return A ResponseEntity containing a success message wrapped in GenericApiResponse.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        // Wrap the success message in the GenericApiResponse
        return ResponseEntity.ok(new ResponseModel<>("User deleted successfully", null));
    }
}
