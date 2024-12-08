package com.wipro.newsapp.user.controller;

import com.wipro.newsapp.user.exception.UserAlreadyExistsException;
import com.wipro.newsapp.user.model.User;
import com.wipro.newsapp.user.service.UserDetailsServiceImpl;
import com.wipro.newsapp.user.service.UserService;
import com.wipro.newsapp.user.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authentication/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        try {
            User u = userService.signup(user);

            return new ResponseEntity<User>(u, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
        }
        return new ResponseEntity<String>("Failed to create user", HttpStatus.CONFLICT);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@ModelAttribute("email") String email,
                                       @ModelAttribute("password") String password) {
        User u = userService.login(email, password);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtility.generateToken(userDetails);
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        map.put("role", userService.getRole(email));
        ResponseEntity<Map<String, String>> response = new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
        return response;
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
    }

    @PatchMapping("/updatePassword")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updatePassword(@RequestBody User user) {
        return new ResponseEntity<User>(userService.updatePassword(user), HttpStatus.OK);
    }

    @PostMapping("/logoutUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(userService.logoutUser(token.substring(7)), HttpStatus.OK);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<String> authenticate() {
        return new ResponseEntity<String>("authentication success", HttpStatus.OK);
    }

}
