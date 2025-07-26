package pjatk.s18617.tournamentmanagement.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pjatk.s18617.tournamentmanagement.model.Role;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

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

}