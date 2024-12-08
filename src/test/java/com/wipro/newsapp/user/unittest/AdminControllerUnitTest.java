package com.wipro.newsapp.user.unittest;

import com.wipro.newsapp.user.controller.AdminController;
import com.wipro.newsapp.user.model.User;
import com.wipro.newsapp.user.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
class AdminControllerUnitTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteUser() {
        String email = "test@example.com";
        when(adminService.deleteUser(email)).thenReturn("User deleted successfully");

        ResponseEntity<String> responseEntity = adminController.deleteUser(email);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User deleted successfully", responseEntity.getBody());
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        users.add(user);

        when(adminService.getAllUsers()).thenReturn(users);

        List<User> responseList = adminController.getAllUsers();

        assertEquals(users.size(), responseList.size());
        assertEquals(users.get(0).getName(), responseList.get(0).getName());
        assertEquals(users.get(0).getEmail(), responseList.get(0).getEmail());
    }
}
