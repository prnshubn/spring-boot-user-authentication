package com.wipro.newsapp.user.unittest;

import com.wipro.newsapp.user.model.Role;
import com.wipro.newsapp.user.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class RoleRepositoryUnitTest {

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testFindAll() {
        // Create some roles
        Role role1 = new Role();
        role1.setRoleName("admin");

        Role role2 = new Role();
        role2.setRoleName("user");

        // Configure the mock to return a list of roles when the findAll method is
        // called
        List<Role> roles = List.of(role1, role2);
        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        // Call the findAll method
        List<Role> result = roleRepository.findAll();

        // Assert that the correct list of roles is returned
        assertEquals(2, result.size());
        assertTrue(result.contains(role1));
        assertTrue(result.contains(role2));
    }

    @Test
    public void testFindByRoleName() {
        // Create some roles
        Role role1 = new Role();
        role1.setRoleName("admin");

        Role role2 = new Role();
        role2.setRoleName("user");
        // Configure the mock to return the role when findByRoleName is called with the
        // role name
        Mockito.when(roleRepository.findByRoleName("admin")).thenReturn(Optional.of(role1));

        // Call the findByRoleName method with the role name
        Optional<Role> result = roleRepository.findByRoleName("admin");

        // Assert that the role name was found
        assertTrue(result.isPresent());
        assertEquals(role1.getRoleName(), result.get().getRoleName());
    }
}
