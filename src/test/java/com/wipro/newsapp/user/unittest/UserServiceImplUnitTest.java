package com.wipro.newsapp.user.unittest;

import com.wipro.newsapp.user.exception.UserAlreadyExistsException;
import com.wipro.newsapp.user.exception.UserNotFoundException;
import com.wipro.newsapp.user.model.BlackListedToken;
import com.wipro.newsapp.user.model.Role;
import com.wipro.newsapp.user.model.User;
import com.wipro.newsapp.user.repository.BlackListedTokenRepository;
import com.wipro.newsapp.user.repository.RoleRepository;
import com.wipro.newsapp.user.repository.UserRepository;
import com.wipro.newsapp.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplUnitTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BlackListedTokenRepository blackListedTokenRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);
        blackListedTokenRepository = mock(BlackListedTokenRepository.class);
        userServiceImpl = new UserServiceImpl(userRepository, bCryptPasswordEncoder, authenticationManager,
                blackListedTokenRepository, roleRepository);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignup() throws UserAlreadyExistsException {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        Role role = new Role();
        role.setRole_id(2L);
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encryptedpassword");

        User savedUser = new User();
        savedUser.setUser_id(1L);
        when(userRepository.save(user)).thenReturn(savedUser);
        User returnedUser = userServiceImpl.signup(user);
        assertEquals(savedUser.getUser_id(), returnedUser.getUser_id());
    }

    @Test
    public void testLogin() {
        User user = new User();
        user.setEmail("test@test.com");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        User returnedUser = userServiceImpl.login("test@test.com", "password");
        assertEquals(user.getEmail(), returnedUser.getEmail());
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new UserNotFoundException("bad credentials!"));

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.login("test@gmail.com", "password"));
    }

    @Test
    public void testDeleteUser() {
        userServiceImpl.deleteUser("test@gmail.com");
    }

    @Test
    void testLogoutUser() {
        String token = "someTokenValue";
        userServiceImpl.logoutUser(token);
        BlackListedToken blackListedToken = new BlackListedToken();
        blackListedToken.setToken_id(1L);
        blackListedToken.setToken(token);
        String result = userServiceImpl.logoutUser(token);
        assertEquals("logout successful", result);
    }

    @Test
    public void testUpdatePassword() {

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("oldpassword");

        User updatedUser = new User();
        updatedUser.setEmail("test@example.com");
        updatedUser.setPassword("newpassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.encode("newpassword")).thenReturn("encodednewpassword");
        when(userRepository.save(user)).thenReturn(updatedUser);

        User result = userServiceImpl.updatePassword(updatedUser);

        assertEquals("test@example.com", result.getEmail());
        assertEquals("newpassword", result.getPassword());

    }
}