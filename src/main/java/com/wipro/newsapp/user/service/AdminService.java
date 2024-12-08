package com.wipro.newsapp.user.service;

import com.wipro.newsapp.user.model.User;

import java.util.List;

public interface AdminService {
    String deleteUser(String userEmail);

    List<User> getAllUsers();

    void initRolesAndUsers();

}
