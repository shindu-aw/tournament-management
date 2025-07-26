package pjatk.s18617.tournamentmanagement.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import pjatk.s18617.tournamentmanagement.model.Role;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private final User userRoleUser = User.builder()
            .username("username")
            .password("password")
            .role(Role.USER)
            .build();

    private final User userRoleAdmin = User.builder()
            .username("username")
            .password("password")
            .role(Role.ADMIN)
            .build();

    @Test
    void checkAdminAuthorization() {
        when(userRepository.findByUsernameIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(userRoleAdmin));

        Assertions.assertDoesNotThrow(() -> userServiceImpl.checkAdminAuthorization("username"));
    }

    @Test
    void checkAdminAuthorizationFail() {
        when(userRepository.findByUsernameIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(userRoleUser));

        Assertions.assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.checkAdminAuthorization("username"));
    }

    @Test
    void checkEditUserAuthorization() {
        when(userRepository.findByUsernameIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(userRoleUser));

        Assertions.assertDoesNotThrow(() -> userServiceImpl.checkEditUserAuthorization(
                userRoleUser, userRoleUser.getUsername())
        );
    }

    @Test
    void checkEditUserAuthorizationFail() {
        when(userRepository.findByUsernameIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(userRoleUser));

        Assertions.assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.checkEditUserAuthorization(userRoleUser, "wrong username"));
    }

    @Test
    void checkEditUserAuthorizationAdmin() {
        when(userRepository.findByUsernameIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(userRoleUser));

        Assertions.assertDoesNotThrow(() -> userServiceImpl.checkEditUserAuthorization(
                userRoleUser, userRoleAdmin.getUsername())
        );
    }

    @Test
    void save() {
        // 1. arrange
        User userToSave = User.builder()
                .username("testuser")
                .password("rawPassword123")
                .build();
        User savedUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("hashedPassword123")
                .build();

        when(passwordEncoder.encode(Mockito.any(String.class))).thenReturn("hashedPassword123");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        // 2. act
        User resultUser = userServiceImpl.save(userToSave);

        // 3. assert

        // verify that passwordEncoder.encode was called exactly once with the raw password
        verify(passwordEncoder).encode("rawPassword123");

        // assert on the returned user object
        assertNotNull(resultUser);
        assertEquals("testuser", resultUser.getUsername());
        assertEquals("hashedPassword123", resultUser.getPassword());
        assertEquals(1L, resultUser.getId());

        // capture the user object passed to userRepository.save
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("testuser", capturedUser.getUsername());
        assertEquals("hashedPassword123", capturedUser.getPassword());
    }

}