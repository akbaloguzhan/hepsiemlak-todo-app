package com.oakbal.todoapp.backend.controller;

import com.oakbal.todoapp.backend.model.User;
import com.oakbal.todoapp.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        when(userService.registerUser(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof User);
        assertEquals("testuser", ((User) response.getBody()).getUsername());
    }

    @Test
    void registerUser_Failure() {
        User user = new User();
        user.setUsername("existinguser");

        when(userService.registerUser(any(User.class))).thenThrow(new RuntimeException("Username already exists"));

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username already exists", response.getBody());
    }

    @Test
    void loginUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        ResponseEntity<?> response = userController.loginUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged in successfully", response.getBody());
    }

    @Test
    void getUserByUsername_Success() {
        User user = new User();
        user.setUsername("testuser");

        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByUsername("testuser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void getUserByUsername_NotFound() {
        when(userService.getUserByUsername("nonexistent")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserByUsername("nonexistent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getCurrentUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getCurrentUser(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
        assertNull(response.getBody().getPassword());
    }

    @Test
    void getCurrentUser_NotFound() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("nonexistent");
        when(userService.getUserByUsername("nonexistent")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getCurrentUser(auth);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void loginUser_WithInvalidCredentials() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        assertThrows(BadCredentialsException.class, () -> userController.loginUser(user));
    }

    @Test
    void registerUser_WithExistingEmail() {
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("existing@example.com");

        when(userService.registerUser(any(User.class))).thenThrow(new RuntimeException("Email already exists"));

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists", response.getBody());
    }
}