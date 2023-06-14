package com.example.springsecuritywithjwt.service;

import com.example.springsecuritywithjwt.config.JwtService;
import com.example.springsecuritywithjwt.dtos.request.AuthenticationRequest;
import com.example.springsecuritywithjwt.dtos.request.RegisterRequest;
import com.example.springsecuritywithjwt.dtos.response.AuthenticationResponse;
import com.example.springsecuritywithjwt.model.Role;
import com.example.springsecuritywithjwt.model.TokenType;
import com.example.springsecuritywithjwt.model.User;
import com.example.springsecuritywithjwt.repository.TokenRepository;
import com.example.springsecuritywithjwt.repository.UserRepository;
import com.example.springsecuritywithjwt.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
       var savedUser = repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);


        return AuthenticationResponse.builder().token(jwtToken).build();

    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
