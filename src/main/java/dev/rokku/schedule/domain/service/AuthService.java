package dev.rokku.schedule.domain.service;

import dev.rokku.schedule.domain.dto.auth.request.CreateUserRequest;
import dev.rokku.schedule.domain.dto.auth.request.LoginRequest;
import dev.rokku.schedule.domain.dto.auth.request.RegisterRequest;
import dev.rokku.schedule.domain.dto.auth.request.UpdateUserRequest;
import dev.rokku.schedule.domain.dto.auth.response.AuthResponse;
import dev.rokku.schedule.domain.dto.auth.response.UserResponse;
import dev.rokku.schedule.domain.exception.ApiException;
import dev.rokku.schedule.domain.model.user.UserRole;
import dev.rokku.schedule.domain.model.user.Users;
import dev.rokku.schedule.domain.repository.UsersRepository;
import dev.rokku.schedule.domain.security.util.JwtUtil;
import dev.rokku.schedule.domain.security.util.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        validarEmailDisponivel(request.email());

        Users user = new Users();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.USER);

        usersRepository.save(user);

        return gerarRespostaAutenticacao(UserDetailsImpl.buildUserDetailsImpl(user));
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        return gerarRespostaAutenticacao((UserDetailsImpl) authentication.getPrincipal());
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        validarEmailDisponivel(request.email());

        Users user = new Users();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        usersRepository.save(user);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listUsers() {
        return usersRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return toResponse(buscaUsuarioPorId(id));
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        Users user = buscaUsuarioPorId(id);

        if (!user.getEmail().equalsIgnoreCase(request.email()) && usersRepository.existsByEmail(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "E-mail ja cadastrado.");
        }

        user.setName(request.name());
        user.setEmail(request.email());
        user.setRole(request.role());

        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        usersRepository.save(user);

        return toResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        Users user = buscaUsuarioPorId(id);
        usersRepository.delete(user);
    }

    private AuthResponse gerarRespostaAutenticacao(UserDetailsImpl userDetails) {
        String token = jwtUtil.generateJwtToken(userDetails);
        return new AuthResponse(token, "Bearer", jwtUtil.getExpiresAt());
    }

    private Users buscaUsuarioPorId(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario nao encontrado."));
    }

    private void validarEmailDisponivel(String email) {
        if (usersRepository.existsByEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "E-mail ja cadastrado.");
        }
    }

    private UserResponse toResponse(Users user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
