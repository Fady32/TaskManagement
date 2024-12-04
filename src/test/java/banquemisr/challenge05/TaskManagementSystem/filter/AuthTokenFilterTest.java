package banquemisr.challenge05.TaskManagementSystem.filter;

import banquemisr.challenge05.TaskManagementSystem.config.JwtTokenUtil;
import banquemisr.challenge05.TaskManagementSystem.config.UserRolesConfiguration;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {


    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserRolesConfiguration userDetailsService;


    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
    }

    @Test
    void testNoAuthorizationHeader() throws Exception {
        // No Authorization header in the request
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Verify filterChain continues without setting authentication
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testInvalidAuthorizationHeader() throws Exception {
        // Invalid Authorization header
        request.addHeader("Authorization", "InvalidToken");

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testValidToken() throws Exception {
        // Mock valid token and user
        String token = "any token";
        String username = "testuser";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "", Collections.emptySet());

        request.addHeader("Authorization", "Bearer " + token);
        when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtTokenUtil.validateToken(token, userDetails)).thenReturn(true);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);

        // Verify that authentication is set
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(username, authentication.getName());
    }

    @Test
    void testInvalidToken() throws Exception {
        // Mock invalid token
        String token = "invalid-token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn(null);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}
