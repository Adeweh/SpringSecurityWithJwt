package com.example.springsecuritywithjwt.service;

import com.example.springsecuritywithjwt.dtos.request.AuthenticationRequest;
import com.example.springsecuritywithjwt.dtos.request.RegisterRequest;
import com.example.springsecuritywithjwt.dtos.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
