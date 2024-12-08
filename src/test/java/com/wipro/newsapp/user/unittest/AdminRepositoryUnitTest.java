package com.wipro.newsapp.user.unittest;

import com.wipro.newsapp.user.model.Role;
import com.wipro.newsapp.user.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AdminRepositoryUnitTest {

    @Mock
    private AdminRepository adminRepository;

    @Test
    public void testFindByRoleName() {
        // Create a role
        Role role = new Role();
        role.setRoleName("admin");

        // Configure the mock to return the role when findByRoleName is called with the
        // test role name
        Mockito.when(adminRepository.findByRoleName("admin")).thenReturn(Optional.of(role));

        // Call the findByRoleName method with the test role name
        Optional<Role> result = adminRepository.findByRoleName("admin");

        // Assert that the role was found
        assertTrue(result.isPresent());
        assertEquals(role.getRoleName(), result.get().getRoleName());
    }
}
