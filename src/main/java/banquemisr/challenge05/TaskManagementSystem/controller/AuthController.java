package banquemisr.challenge05.TaskManagementSystem.controller;

import banquemisr.challenge05.TaskManagementSystem.dto.AuthRequest;
import banquemisr.challenge05.TaskManagementSystem.dto.AuthResponse;
import banquemisr.challenge05.TaskManagementSystem.dto.ResponseModel;
import banquemisr.challenge05.TaskManagementSystem.dto.UserRequestDto;
import banquemisr.challenge05.TaskManagementSystem.service.AuthService;
import banquemisr.challenge05.TaskManagementSystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService; // Service to handle user-related logic
    private final BCryptPasswordEncoder passwordEncoder; // Password encoder for encrypting user passwords

    // Endpoint for user login (authentication)
    @Operation(security = {})
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        // Authenticate and generate a token
        String token = authService.authenticate(authRequest).getToken();
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Registers a new user and returns a success message.
     *
     * @param user The user details for registration.
     * @return A ResponseEntity containing a success message with HTTP status 201 (Created).
     */
    @Operation(security = {})
    @PostMapping("/register")
    public ResponseEntity<ResponseModel<String>> registerUser(@Valid @RequestBody UserRequestDto user) {
        // Encrypt the user's password before saving to the database
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the database
        userService.saveUser(user);

        // Wrap the response in GenericApiResponse
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel<>("User registered successfully", null));
    }

}
