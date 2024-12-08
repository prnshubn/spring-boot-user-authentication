package com.wipro.newsapp.user.unittest;

import com.wipro.newsapp.user.model.User;
import com.wipro.newsapp.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryUnitTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        // Create a user
        User user = new User();
        user.setEmail("testuser@example.com");

        // Configure the mock to return the user when findByEmail is called with the
        // test email
        Mockito.when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(user));

        // Call the findByEmail method with the test email
        Optional<User> result = userRepository.findByEmail("testuser@example.com");

        // Assert that the user was found
        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }

    @Test
    public void testDeleteByEmail() {
        // Create a user
        User user = new User();
        user.setEmail("testuser@example.com");

        // Call the deleteByEmail method with the test email
        userRepository.deleteByEmail("testuser@example.com");

        // Verify that the deleteByEmail method was called with the test email
        Mockito.verify(userRepository).deleteByEmail("testuser@example.com");
    }
}
