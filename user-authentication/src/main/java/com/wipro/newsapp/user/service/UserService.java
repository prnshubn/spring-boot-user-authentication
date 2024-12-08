package com.wipro.newsapp.user.service;

import com.wipro.newsapp.user.model.User;

public interface UserService {
    User signup(User user);

    User login(String email, String password);

    User updatePassword(User user);

    void deleteUser(String email);

    String logoutUser(String token);

    String getRole(String email);

}
