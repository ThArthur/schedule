package dev.rokku.schedule.domain.security;

import java.util.List;

public class UriShouldNotFilter {

    public static final List<String> JWT_AUTH_FILTER_EXCLUDED_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register"
    );

}
