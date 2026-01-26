package alvino.dev.challenge_forohub.infra.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        Object message,
        LocalDateTime timestamp
) {
    public ErrorResponse(int status, String error, Object message) {
        this(status, error, message, LocalDateTime.now());
    }
}