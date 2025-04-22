package com.akt.vms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akt.vms.dto.AuthRequest;
import com.akt.vms.entity.User;
import com.akt.vms.repository.UserRepository;
import com.akt.vms.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(jwtUtil.generateToken(user.getUsername()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}