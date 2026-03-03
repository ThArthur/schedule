package dev.rokku.schedule.domain.security.filter;

import dev.rokku.schedule.domain.security.UriShouldNotFilter;
import dev.rokku.schedule.domain.security.util.HttpFilterHelper;
import dev.rokku.schedule.domain.security.util.JwtUtil;
import dev.rokku.schedule.domain.security.util.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = HttpFilterHelper.extractToken(request);

        if (token != null) {
            try {
                jwtUtils.assertValidToken(token);
                String idUsuario = jwtUtils.getIdUsuarioFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserById(Long.parseLong(idUsuario));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (AuthenticationException e) {
                HttpFilterHelper.writeJsonError(response, HttpStatus.UNAUTHORIZED, e.getMessage());
                return;
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                        "Bearer error=\"invalid_token\", error_description=\"The access token expired\"");
                response.setContentType("application/json");
                response.getWriter().write("{\"erro\":\"token_expired\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();

        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        return UriShouldNotFilter.JWT_AUTH_FILTER_EXCLUDED_PATHS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

}
