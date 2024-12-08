package com.wipro.newsapp.user.unittest;

import com.wipro.newsapp.user.model.User;
import com.wipro.newsapp.user.repository.UserRepository;
import com.wipro.newsapp.user.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AdminServiceImplUnitTest {

    @Autowired
    private AdminService adminService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setUser_id(1L);
        user1.setEmail("test1@example.com");

        User user2 = new User();
        user2.setUser_id(2L);
        user2.setEmail("test2@example.com");

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = adminService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(userList.size(), result.size());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUser_id(1L);
        user.setEmail("test@example.com");
        user.setActive(true);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        String result = adminService.deleteUser(user.getEmail());

        assertEquals("delete success", result);
    }
}