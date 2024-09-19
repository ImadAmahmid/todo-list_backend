package com.keyconsulting.todolist.app.service;

import com.keyconsulting.todolist.app.controller.dto.AuthenticationRequest;
import com.keyconsulting.todolist.app.controller.dto.AuthenticationResponse;
import com.keyconsulting.todolist.app.controller.dto.RegisterRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
