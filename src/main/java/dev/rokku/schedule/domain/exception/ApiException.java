package dev.rokku.schedule.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public static ApiException noContentException() {
        return new ApiException(HttpStatus.NO_CONTENT, "");
    }
}
