package com.wipro.newsapp.user.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.newsapp.user.controller.UserController;
import com.wipro.newsapp.user.exception.UserAlreadyExistsException;
import com.wipro.newsapp.user.model.User;
import com.wipro.newsapp.user.service.UserDetailsServiceImpl;
import com.wipro.newsapp.user.service.UserService;
import com.wipro.newsapp.user.util.JwtUtility;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerUnitTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtility jwtUtility;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testAddNewUser() throws Exception {
        User user = User.builder().email("test@example.com").password("password").build();
        when(userService.signup(ArgumentMatchers.any())).thenReturn(user);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(
                        post("/authentication/user/signup").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .isEqualTo(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void testAddNewUserWithValidUser() throws Exception {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        when(userService.signup(user)).thenReturn(user);

        // Act
        ResponseEntity<?> response = userController.addNewUser(user);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User responseBody = (User) response.getBody();
        assertNotNull(responseBody);
        assertEquals(user, responseBody);
    }

    @Test
    public void testAddNewUserWithExistingUser() throws Exception {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        when(userService.signup(user)).thenThrow(UserAlreadyExistsException.class);

        // Act
        ResponseEntity<?> response = userController.addNewUser(user);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        String responseBody = (String) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Failed to create user", responseBody);
    }

    @Test
    public void testLoginUser() {
        // Create a test user and user details
        String email = "test@example.com";
        String password = "password";
        User testUser = new User();
        testUser.setEmail(email);
        testUser.setPassword(password);
        UserDetails testUserDetails = userDetailsService.loadUserByUsername(email);

        // Mock the UserService to return the test user
        when(userService.login(email, password)).thenReturn(testUser);

        // Mock the JwtUtility to return a test token
        String testToken = "testToken";
        when(jwtUtility.generateToken(testUserDetails)).thenReturn(testToken);

        // Call the controller method
        ResponseEntity<Map<String, String>> response = (ResponseEntity<Map<String, String>>) userController
                .loginUser(email, password);

        // Verify that the response is a success (HTTP 200 OK)
        assert (response.getStatusCode() == HttpStatus.OK);

        // Verify that the response contains a map with the expected token value
        Map<String, String> responseBody = response.getBody();
        assert (responseBody.containsKey("token"));
        assert (responseBody.get("token").equals(testToken));
    }

    @Test
    void testAddNewUserWhenUserAlreadyExists() throws Exception {
        User user = User.builder().email("test@example.com").password("password").build();
        when(userService.signup(ArgumentMatchers.any()))
                .thenThrow(new UserAlreadyExistsException("user with this email already exists"));

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(
                        post("/authentication/user/signup").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .isEqualTo("Failed to create user"));
    }

    @Test
    public void testUpdatePassword() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        when(userService.updatePassword(user)).thenReturn(user);

        // Act
        ResponseEntity<?> response = userController.updatePassword(user);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseBody = (User) response.getBody();
        assertNotNull(responseBody);
        assertEquals(user, responseBody);
    }

    @Test
    public void testLogoutUser() {
        String token = "Bearer abc123";
        String expectedResponse = "Logged out successfully";
        when(userService.logoutUser("abc123")).thenReturn(expectedResponse);

        ResponseEntity<String> response = userController.logoutUser(token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        String email = "test@test.com";

        // Act
        ResponseEntity<String> response = userController.deleteUser(email);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Successfully Deleted", responseBody);
        verify(userService, times(1)).deleteUser(email);
    }
}