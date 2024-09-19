package com.keyconsulting.todolist.app.controller;

import com.keyconsulting.todolist.app.controller.dto.AuthenticationRequest;
import com.keyconsulting.todolist.app.controller.dto.AuthenticationResponse;
import com.keyconsulting.todolist.app.controller.dto.RegisterRequest;
import com.keyconsulting.todolist.app.controller.resource.AutheticationApi;
import com.keyconsulting.todolist.app.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth management APIs")
@Slf4j
public class AuthenticationController implements AutheticationApi {

    private final AuthenticationService service;

    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        LOG.info("[Auth Controller] Register user | userEmail={}", request.getEmail());

        return new ResponseEntity<>(service.register(request), HttpStatus.OK);
    }

    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        LOG.info("[Auth Controller] Authenticate user | userEmail={}", request.getEmail());

        return new ResponseEntity<>(service.authenticate(request), HttpStatus.OK);
    }

}
