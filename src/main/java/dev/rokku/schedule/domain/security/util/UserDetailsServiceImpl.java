package dev.rokku.schedule.domain.security.util;

import dev.rokku.schedule.domain.exception.ApiException;
import dev.rokku.schedule.domain.model.user.Users;
import dev.rokku.schedule.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    private static Supplier<ApiException> throwUsuarioNaoEncontrado() {
        return () -> new ApiException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado");
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String login) throws UsernameNotFoundException {

            return usersRepository.findByEmail(login)
                    .map(this::validarLogin)
                    .orElseThrow(throwUsuarioNaoEncontrado());
    }

    public UserDetails loadUserById(Long id) {

        return usersRepository.findById(id)
                .map(this::validarLogin)
                .orElseThrow(throwUsuarioNaoEncontrado());
    }

    private UserDetailsImpl validarLogin(Users usuario) {
        return UserDetailsImpl.buildUserDetailsImpl(usuario);
    }
}
