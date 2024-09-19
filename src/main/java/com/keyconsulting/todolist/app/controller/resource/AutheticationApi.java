package com.keyconsulting.todolist.app.controller.resource;

import com.keyconsulting.todolist.app.controller.dto.AuthenticationRequest;
import com.keyconsulting.todolist.app.controller.dto.AuthenticationResponse;
import com.keyconsulting.todolist.app.controller.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@CrossOrigin(origins = "http://localhost:4200")
public interface AutheticationApi {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/auth/register",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request);

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/auth/authenticate",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request);
}
