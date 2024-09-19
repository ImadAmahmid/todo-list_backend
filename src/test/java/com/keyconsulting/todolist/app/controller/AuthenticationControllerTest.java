package com.keyconsulting.todolist.app.controller;

import com.keyconsulting.todolist.app.controller.dto.*;
import com.keyconsulting.todolist.app.repo.TokenRepository;
import com.keyconsulting.todolist.app.repo.UserRepository;
import com.keyconsulting.todolist.app.service.AuthenticationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;

/**
 * Integration tests
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {
    private static final String USER_EMAIL1 = "user1@gmail.com";
    private static final String USER_EMAIL2 = "user2@gmail.com";
    private static final String PASSWORD1 = "DummyPass";
    private static final String PASSWORD2 = "DummyPass";

    // bind the above RANDOM_PORT
    @LocalServerPort
    private int port;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void cleanUp() {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test registering and authentication")
    public void registerAndAuthentication_success() throws MalformedURLException, InterruptedException {
        RegisterRequest registerBody = RegisterRequest.builder().email(USER_EMAIL1).password(PASSWORD1).build();
        HttpEntity<RegisterRequest> request = new HttpEntity<>(registerBody, new HttpHeaders());

        ResponseEntity<AuthenticationResponse> loginResponse =
                makeRequest(HttpMethod.POST, "http://localhost:" + port + "/api/v1/auth/register", request, AuthenticationResponse.class);

        Assertions.assertThat(loginResponse.getBody().getAccessToken()).isNotBlank();

        // The generated token ended up being the exact same one which lead to some constraint issues while persisting it!
        Thread.sleep(100);

        // authenticate:
        AuthenticationRequest authenticationBody = AuthenticationRequest.builder().email(USER_EMAIL1).password(PASSWORD1).build();
        HttpEntity<AuthenticationRequest> request2 = new HttpEntity<>(authenticationBody, new HttpHeaders());

        ResponseEntity<AuthenticationResponse> autheticationReponse =
                makeRequest(HttpMethod.POST, "http://localhost:" + port + "/api/v1/auth/authenticate", request2, AuthenticationResponse.class);

        Assertions.assertThat(autheticationReponse.getBody().getAccessToken()).isNotBlank();

        // authenticate:
        AuthenticationRequest authenticationBodyWithWrongPass = AuthenticationRequest.builder().email(USER_EMAIL1).password(PASSWORD2).build();
        HttpEntity<AuthenticationRequest> requestFailing = new HttpEntity<>(authenticationBodyWithWrongPass, new HttpHeaders());

        ResponseEntity<AuthenticationResponse> failedAutheticationReponse =
                makeRequest(HttpMethod.POST, "http://localhost:" + port + "/api/v1/auth/authenticate", requestFailing, AuthenticationResponse.class);

        Assertions.assertThat(failedAutheticationReponse.getStatusCode().is2xxSuccessful()).isFalse();
    }

    public <T> ResponseEntity<T> makeRequest(HttpMethod method, String endpoint, HttpEntity<?> request, Class<T> responseType) {
        return restTemplate.exchange(
                endpoint,
                method,
                request,
                responseType);
    }
}