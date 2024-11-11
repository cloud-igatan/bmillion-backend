package com.example.bmillion_backend.controller;

import com.example.bmillion_backend.dto.UserRequestDto;
import com.example.bmillion_backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registering(@RequestBody UserRequestDto registerRequest, HttpServletResponse response) {
        userService.register(registerRequest, response);
        return ResponseEntity.ok("가입 성공, 헤더에 토큰 확인");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDto loginRequest, HttpServletResponse response) {
        userService.normalLogin(loginRequest, response);
        return ResponseEntity.ok("로그인 성공, 헤더에 토큰 확인");
    }

}