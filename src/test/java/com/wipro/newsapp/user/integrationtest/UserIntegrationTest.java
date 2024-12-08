package com.wipro.newsapp.user.integrationtest;

import com.wipro.newsapp.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void testAddNewUser() {
        String URL = "http://localhost:" + port + "/authentication/user/signup";
        User user = new User();
        user.setUser_id(1L);
        user.setName("test1");
        user.setEmail("test1@test.com");
        user.setPassword("password");
        user.setActive(true);
        user.setCreatedOn(LocalDate.now());
        ResponseEntity<User> response = restTemplate.postForEntity(URL, user, User.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getUser_id());
    }

    @Test
    public void testAddExistingUser() {
        String URL = "http://localhost:" + port + "/authentication/user/signup";

        User user1 = new User();
        user1.setUser_id(1L);
        user1.setName("test2");
        user1.setEmail("test2@test.com");
        user1.setPassword("password");
        user1.setActive(true);
        user1.setCreatedOn(LocalDate.now());
        restTemplate.postForEntity(URL, user1, User.class);

        User user2 = new User();
        user2.setUser_id(2L);
        user2.setName("test2");
        user2.setEmail("test2@test.com");
        user2.setPassword("password");
        user2.setActive(true);
        user2.setCreatedOn(LocalDate.now());

        ResponseEntity<String> response = restTemplate.postForEntity(URL, user2, String.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testLoginUserWithIncorrectCredentials() {
        String URL = "http://localhost:" + port + "/authentication/user/login";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("email", "test1@test.com");
        body.add("password", "wrongpassword");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<Map> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(body, headers),
                Map.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testLoginUser() {
        String URL = "http://localhost:" + port + "/authentication/user/login";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("email", "test1@test.com");
        body.add("password", "password");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<Map> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(body, headers),
                Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("token"));
    }

    @Test
    public void testDeleteUser() {
        String email = "test3@test.com";
        String URL = "http://localhost:" + port + "/authentication/user/login";
        User user1 = new User();
        user1.setUser_id(1L);
        user1.setName("test2");
        user1.setEmail("test2@test.com");
        user1.setPassword("password");
        user1.setActive(true);
        user1.setCreatedOn(LocalDate.now());
        restTemplate.postForEntity(URL, user1, User.class);
        String deleteURL = "http://localhost:" + port + "/delete/{email}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Setup Request Entity
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(deleteURL, HttpMethod.DELETE, requestEntity,
                String.class, "test@example.com");
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

}
