package com.wipro.newsapp.user;

import com.wipro.newsapp.user.controller.AdminController;
import com.wipro.newsapp.user.controller.UserController;
import com.wipro.newsapp.user.service.AdminServiceImpl;
import com.wipro.newsapp.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class UserAuthenticationApplicationTests {

    @Autowired
    AdminController adminController;

    @Autowired
    UserController userController;

    @Autowired
    AdminServiceImpl adminServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Test
    void contextLoads() {
        assertThat(adminController).isNotNull();
        assertThat(userController).isNotNull();
        assertThat(adminServiceImpl).isNotNull();
        assertThat(userServiceImpl).isNotNull();
    }

}
