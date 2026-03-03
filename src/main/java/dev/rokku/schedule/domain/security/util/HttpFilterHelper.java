package dev.rokku.schedule.domain.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class HttpFilterHelper {

    public static String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }

        if (!authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring(7).trim();
    }

    public static void writeJsonError(HttpServletResponse response,
                                      HttpStatus status,
                                      String message) throws IOException {

        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        String body = String.format("{\"message\":\"%s\"}", escape(message));
        response.getWriter().write(body);
        response.getWriter().flush();
    }

    public static void writeJsonError(HttpServletResponse response,
                                      HttpStatus status,
                                      String message,
                                      Map<String, String> headers) throws IOException {

        response.setStatus(status.value());

        if (headers != null) {
            headers.forEach(response::setHeader);
        }

        response.setContentType("application/json;charset=UTF-8");
        String body = String.format("{\"message\":\"%s\"}", escape(message));
        response.getWriter().write(body);
        response.getWriter().flush();
    }


    public static String escape(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}

