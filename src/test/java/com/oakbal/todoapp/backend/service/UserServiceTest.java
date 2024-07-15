package com.oakbal.todoapp.backend.service;

import com.oakbal.todoapp.backend.model.User;
import com.oakbal.todoapp.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("User", result.getType());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_UsernameExists() {
        User user = new User();
        user.setUsername("existinguser");
        user.setEmail("test@example.com");

        when(userRepository.findUserByUsername("existinguser")).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> userService.registerUser(user));
    }

    @Test
    void registerUser_EmailExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("existing@example.com");

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail("existing@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> userService.registerUser(user));
    }

    @Test
    void getUserByUsername_Success() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void getUserByUsername_NotFound() {
        when(userRepository.findUserByUsername("nonexistent")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUsername("nonexistent");

        assertFalse(result.isPresent());
    }

    @Test
    void getUserIdByUsername_Success() {
        User user = new User();
        user.setId("123");
        user.setUsername("testuser");

        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        String result = userService.getUserIdByUsername("testuser");

        assertEquals("123", result);
    }

    @Test
    void getUserIdByUsername_NotFound() {
        when(userRepository.findUserByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserIdByUsername("nonexistent"));
    }

    @Test
    void loadUserByUsername_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("USER")));
    }

    @Test
    void loadUserByUsername_NotFound() {
        when(userRepository.findUserByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent"));
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        when(userRepository.findUserByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent"));
    }
}