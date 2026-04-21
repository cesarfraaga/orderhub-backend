package com.core.orderhub.backend.controller;

import com.core.orderhub.backend.domain.entity.User;
import com.core.orderhub.backend.dto.LoginRequestDto;
import com.core.orderhub.backend.dto.LoginResponseDto;
import com.core.orderhub.backend.dto.RegisterRequestDto;
import com.core.orderhub.backend.repository.UserRepository;
import com.core.orderhub.backend.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDto dto) {

        var authToken = new UsernamePasswordAuthenticationToken(
                dto.getEmail(),
                dto.getPassword()
        );

        authenticationManager.authenticate(authToken);

        var user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        var user = new User(dto.getEmail(), passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}