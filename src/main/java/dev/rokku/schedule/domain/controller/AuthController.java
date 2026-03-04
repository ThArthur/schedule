package dev.rokku.schedule.domain.controller;

import dev.rokku.schedule.domain.dto.auth.request.CreateUserRequest;
import dev.rokku.schedule.domain.dto.auth.request.LoginRequest;
import dev.rokku.schedule.domain.dto.auth.request.RegisterRequest;
import dev.rokku.schedule.domain.dto.auth.request.UpdateUserRequest;
import dev.rokku.schedule.domain.dto.auth.response.AuthResponse;
import dev.rokku.schedule.domain.dto.auth.response.UserResponse;
import dev.rokku.schedule.domain.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
