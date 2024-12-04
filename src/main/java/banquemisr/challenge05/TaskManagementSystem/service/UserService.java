package banquemisr.challenge05.TaskManagementSystem.service;


import banquemisr.challenge05.TaskManagementSystem.dto.UserRequestDto;
import banquemisr.challenge05.TaskManagementSystem.dto.UserResponseDto;
import banquemisr.challenge05.TaskManagementSystem.exception.UserNotFoundException;
import banquemisr.challenge05.TaskManagementSystem.model.User;
import banquemisr.challenge05.TaskManagementSystem.repo.UserRepo;
import banquemisr.challenge05.TaskManagementSystem.util.UserConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepo userRepository;

    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Save a new user in the database.
     *
     * @param user the user to save
     */
    public void saveUser(UserRequestDto user) {
        User userEntity = UserConverter.toEntity(user);
        userRepository.save(userEntity);
    }

    /**
     * Retrieve a paginated list of all users.
     *
     * @param pageable the pagination parameters
     * @return a paginated list of users as Page<UserResponseDto>
     */
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        // Get the paginated users from the repository
        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.map(UserConverter::toResponse);  // Return the Page of UserResponseDto
    }

    /**
     * Retrieve a specific user by their ID.
     *
     * @param username the Username of the user to retrieve
     * @return the user
     * @throws UserNotFoundException if the user is not found
     */
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

        return UserConverter.toResponse(user);
    }

    /**
     * Delete a user by their ID.
     *
     * @param id the ID of the user to delete
     * @throws UserNotFoundException if the user is not found
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public User getCurrentLoggedUser() throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                return userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        }
        throw new UsernameNotFoundException("User not found");
    }

    /**
     * Find users by their usernames.
     *
     * @param usernames The set of usernames to find.
     * @return The set of users.
     */
    public Set<User> findUsersByUsernames(Set<String> usernames) {
        return new HashSet<>(userRepository.findAllByUsernameIn(usernames));
    }
}
