package com.wipro.newsapp.user.controller;

import com.wipro.newsapp.user.annotation.Generated;
import com.wipro.newsapp.user.model.User;
import com.wipro.newsapp.user.service.AdminService;
import com.wipro.newsapp.user.service.UserDetailsServiceImpl;
import com.wipro.newsapp.user.util.JwtUtility;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Generated
    @PostConstruct
    public void initRolesAndUsers() {
        adminService.initRolesAndUsers();
    }

    @DeleteMapping("/delete_user/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        return new ResponseEntity<>(adminService.deleteUser(email), HttpStatus.OK);
    }

    @GetMapping("/get_all_users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

}
