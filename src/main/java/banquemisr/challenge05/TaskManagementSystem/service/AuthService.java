package banquemisr.challenge05.TaskManagementSystem.service;
import banquemisr.challenge05.TaskManagementSystem.config.JwtTokenUtil;
import banquemisr.challenge05.TaskManagementSystem.config.UserRolesConfiguration;
import banquemisr.challenge05.TaskManagementSystem.dto.AuthRequest;
import banquemisr.challenge05.TaskManagementSystem.dto.AuthResponse;
import banquemisr.challenge05.TaskManagementSystem.exception.UserNotFoundException;
import banquemisr.challenge05.TaskManagementSystem.model.User;
import banquemisr.challenge05.TaskManagementSystem.repo.UserRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepo userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRolesConfiguration userDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepo userRepository, AuthenticationManager authenticationManager,
                       JwtTokenUtil jwtTokenUtil, UserRolesConfiguration userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        Optional<User> user = userRepository.findByUsername(authRequest.getUsername());

        if (user.isPresent()) {
            String token = jwtTokenUtil.generateToken(userDetails, user.get().getRoles());
            return new AuthResponse(token);
        }
        throw new UserNotFoundException("User not found");
    }
}








