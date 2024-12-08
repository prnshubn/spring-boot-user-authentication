package com.wipro.newsapp.user.service;

import com.wipro.newsapp.user.exception.UserAlreadyExistsException;
import com.wipro.newsapp.user.exception.UserNotFoundException;
import com.wipro.newsapp.user.model.BlackListedToken;
import com.wipro.newsapp.user.model.Role;
import com.wipro.newsapp.user.model.User;
import com.wipro.newsapp.user.repository.BlackListedTokenRepository;
import com.wipro.newsapp.user.repository.RoleRepository;
import com.wipro.newsapp.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BlackListedTokenRepository blackListedTokenRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                           AuthenticationManager authenticationManager, BlackListedTokenRepository blackListedTokenRepository,
                           RoleRepository roleRepository) {
        super();
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.blackListedTokenRepository = blackListedTokenRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User signup(User user) throws UserAlreadyExistsException {
        Role role = roleRepository.findById(2L).orElseThrow();
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Optional<User> u = userRepository.findByEmail(user.getEmail());
        if (u.isPresent())
            throw new UserAlreadyExistsException("user with this email already exists");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setCreatedOn(LocalDate.now());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("bad credentials!");
        }
        return userRepository.findByEmail(email).get();
    }

    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    public User updatePassword(User user) {
        User u = userRepository.findByEmail(user.getEmail()).orElseThrow();
        u.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(u);
    }

    @Override
    public String logoutUser(String token) {
        BlackListedToken blackListedToken = new BlackListedToken();
        blackListedToken.setToken(token);
        blackListedTokenRepository.save(blackListedToken);
        return "logout successful";
    }

    @Override
    public String getRole(String email) {
        User u = userRepository.findByEmail(email).orElseThrow();
        Set<Role> roleSet = u.getRoles();
        Role[] roleArray = roleSet.toArray(new Role[roleSet.size()]);
        return roleArray[0].getRoleName();
    }

}
