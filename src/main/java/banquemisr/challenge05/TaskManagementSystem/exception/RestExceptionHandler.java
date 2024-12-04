package banquemisr.challenge05.TaskManagementSystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getLocalizedMessage());
    }

    // Handle validation exceptions (ValidationException)
    @ExceptionHandler( value = {ValidationException.class})
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fasdy");
    }

    // Handle validation exceptions (IllegalArgumentException)
    @ExceptionHandler( value = {IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalArgumentExceptionException(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
    }

    // Handle validation exceptions (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();

        // Collect all error messages into a list
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            String detailedErrorMessage = String.format("Field '%s': %s", fieldName, errorMessage);
            errorMessages.add(detailedErrorMessage); // Add descriptive error message to the list
        });

        log.error("Validation failed with errors: {}", errorMessages, ex);

        return ResponseEntity.badRequest().body(errorMessages); // Return a list of errors
    }

    // Handle UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        log.warn("User not found!");
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Handle InsufficientAuthenticationException (when a user is not authenticated)
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<String> handleInsufficientAuthException(InsufficientAuthenticationException ex) {
        log.warn("Insufficient authentication!");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED.toString(), HttpStatus.UNAUTHORIZED);
    }

    // Handle AuthenticationException (for general authentication issues)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication failed!");
        return new ResponseEntity<>("Authentication failed: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Handle AccessDeniedException (for cases where user does not have permission)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied!");
        return new ResponseEntity<>("Access denied: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // Handle generic HttpClientErrorException for 401 or 403 status codes
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            log.warn("Unauthorized access attempt: {}", ex.getMessage());
            return new ResponseEntity<>("Unauthorized access: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
        } else if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
            log.warn("Forbidden access attempt: {}", ex.getMessage());
            return new ResponseEntity<>("Forbidden access: " + ex.getMessage(), HttpStatus.FORBIDDEN);
        } else {
            log.error("Client error: {}", ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
        }
    }
}
