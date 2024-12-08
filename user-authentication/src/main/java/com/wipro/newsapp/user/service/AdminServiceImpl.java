package com.wipro.newsapp.user.service;

import com.wipro.newsapp.user.annotation.Generated;
import com.wipro.newsapp.user.model.Role;
import com.wipro.newsapp.user.model.User;
import com.wipro.newsapp.user.repository.AdminRepository;
import com.wipro.newsapp.user.repository.BlackListedTokenRepository;
import com.wipro.newsapp.user.repository.RoleRepository;
import com.wipro.newsapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BlackListedTokenRepository blackListedTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository2, UserRepository userRepository2,
                            RoleRepository roleRepository2, AuthenticationManager authenticationManager2,
                            BlackListedTokenRepository blackListedTokenRepository2, BCryptPasswordEncoder bCryptPasswordEncoder2) {
        // TODO Auto-generated constructor stub
    }

    @Generated
    public void initRolesAndUsers() {
        Role adminRole = new Role();
        adminRole.setRoleName("ROLE_ADMIN");
        adminRole.setRoleDescription("admin role");
        // roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        userRole.setRoleDescription("default role");
        // roleRepository.save(userRole);

        User adminUser = new User();
        adminUser.setEmail("admin");
        adminUser.setName("admin");
        adminUser.setActive(true);
        adminUser.setCreatedOn(LocalDate.now());
        adminUser.setPassword(bCryptPasswordEncoder.encode("admin"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRoles(adminRoles);
        userRepository.save(adminUser);

        User user = new User();
        user.setEmail("priyanshu@gmail.com");
        user.setName("Priyanshu");
        user.setActive(true);
        user.setCreatedOn(LocalDate.now());
        user.setPassword(bCryptPasswordEncoder.encode("1234"));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    @Override
    public String deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user doesn't exist"));
        userRepository.delete(user);
        return "delete success";
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
