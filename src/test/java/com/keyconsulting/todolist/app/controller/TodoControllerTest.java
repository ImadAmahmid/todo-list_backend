package com.keyconsulting.todolist.app.controller;

import com.keyconsulting.todolist.app.controller.dto.*;
import com.keyconsulting.todolist.app.enums.Status;
import com.keyconsulting.todolist.app.repo.TodoRepository;
import com.keyconsulting.todolist.app.repo.TokenRepository;
import com.keyconsulting.todolist.app.repo.UserRepository;
import com.keyconsulting.todolist.app.service.AuthenticationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
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
public class TodoControllerTest {
    private static final String USER_EMAIL1 = "user1@gmail.com";
    private static final String USER_EMAIL2 = "user2@gmail.com";
    private static final String PASSWORD1 = "DummyPass";
    private static final String DTO_TITLE1 = "Title 1";
    private static final String DTO_DESC1 = "Desc 1";


    // bind the above RANDOM_PORT
    @LocalServerPort
    private int port;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    @AfterEach
    public void cleanUp() {
        todoRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test creating valid todos")
    public void createUpdate_success() throws MalformedURLException {
        AuthenticationResponse authenticationResponse = authenticationService.register(RegisterRequest.builder().email(USER_EMAIL1).password(PASSWORD1).build());

        TodoDto newDto = TodoDto.builder().title(DTO_TITLE1).description(DTO_DESC1).status(Status.NEW).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticationResponse.getAccessToken());

        HttpEntity<TodoDto> request = new HttpEntity<>(newDto, headers);

        ResponseEntity<TodoDto> saveTodoResponse =
                makeRequest(HttpMethod.POST, "http://localhost:" + port + "/api/v1/todo/", request, TodoDto.class);

        Assertions.assertThat(saveTodoResponse.getBody().getTitle()).isEqualTo(newDto.getTitle());
        Assertions.assertThat(saveTodoResponse.getBody().getDescription()).isEqualTo(newDto.getDescription());
        Assertions.assertThat(saveTodoResponse.getBody().getStatus()).isEqualTo(newDto.getStatus());

        TodoDto updatedTodo = TodoDto.builder().id(saveTodoResponse.getBody().getId()).title(DTO_TITLE1).description(DTO_DESC1).status(Status.COMPLETED).build();


        HttpEntity<TodoDto> request2 = new HttpEntity<>(updatedTodo, headers);
        ResponseEntity<TodoDto> updateTodoResponse =
                makeRequest(HttpMethod.POST, "http://localhost:" + port + "/api/v1/todo/", request2, TodoDto.class);

        Assertions.assertThat(updateTodoResponse.getBody().getStatus()).isEqualTo(updatedTodo.getStatus());
    }

    @Test
    @DisplayName("Test creating non valid todos with an empty Title")
    public void createUpdate_fail() throws MalformedURLException {
        AuthenticationResponse authenticationResponse = authenticationService.register(RegisterRequest.builder().email(USER_EMAIL1).password(PASSWORD1).build());

        TodoDto newDto = TodoDto.builder().description(DTO_DESC1).status(Status.NEW).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticationResponse.getAccessToken());

        HttpEntity<TodoDto> request = new HttpEntity<>(newDto, headers);

        ResponseEntity<ErrorDto> saveTodoResponse =
                makeRequest(HttpMethod.POST, "http://localhost:" + port + "/api/v1/todo/", request, ErrorDto.class);

        Assertions.assertThat(saveTodoResponse.getStatusCode().is2xxSuccessful()).isFalse();
    }

    @Test
    @DisplayName("Test deleting todos that do not belong to the user")
    public void delete_fail() throws MalformedURLException {
        AuthenticationResponse authenticationResponse = authenticationService.register(RegisterRequest.builder().email(USER_EMAIL1).password(PASSWORD1).build());
        AuthenticationResponse authenticationResponse2 = authenticationService.register(RegisterRequest.builder().email(USER_EMAIL2).password(PASSWORD1).build());

        TodoDto newDto = TodoDto.builder().title(DTO_TITLE1).description(DTO_DESC1).status(Status.NEW).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticationResponse.getAccessToken());

        HttpEntity<TodoDto> request = new HttpEntity<>(newDto, headers);

        ResponseEntity<TodoDto> saveTodoResponse =
                makeRequest(HttpMethod.POST, "http://localhost:" + port + "/api/v1/todo/", request, TodoDto.class);


        // second user trying to delete it will fail
        HttpHeaders headers2 = new HttpHeaders();
        headers.setBearerAuth(authenticationResponse2.getAccessToken());

        HttpEntity<TodoDto> request2 = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> deleteResponse =
                makeRequest(HttpMethod.DELETE, "http://localhost:" + port + "/api/v1/todo/" + saveTodoResponse.getBody().getId(), request2, ErrorDto.class);

        Assertions.assertThat(deleteResponse.getStatusCode().is2xxSuccessful()).isFalse();
    }


    @Test
    public void delete_success() throws MalformedURLException {
        AuthenticationResponse authenticationResponse = authenticationService.register(RegisterRequest.builder().email(USER_EMAIL1).password(PASSWORD1).build());

        TodoDto newDto = TodoDto.builder().title(DTO_TITLE1).description(DTO_DESC1).status(Status.NEW).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticationResponse.getAccessToken());

        HttpEntity<TodoDto> request = new HttpEntity<>(newDto, headers);

        ResponseEntity<TodoDto> saveTodoResponse =
                makeRequest(HttpMethod.POST, "http://localhost:" + port + "/api/v1/todo/", request, TodoDto.class);


        HttpEntity<TodoDto> request2 = new HttpEntity<>(headers);
        ResponseEntity<TodoDto> deleteResponse =
                makeRequest(HttpMethod.DELETE, "http://localhost:" + port + "/api/v1/todo/" + saveTodoResponse.getBody().getId(), request2, TodoDto.class);

        Assertions.assertThat(deleteResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }

    public <T> ResponseEntity<T> makeRequest(HttpMethod method, String endpoint, HttpEntity<?> request, Class<T> responseType) {
        return restTemplate.exchange(
                endpoint,
                method,
                request,
                responseType);
    }
}