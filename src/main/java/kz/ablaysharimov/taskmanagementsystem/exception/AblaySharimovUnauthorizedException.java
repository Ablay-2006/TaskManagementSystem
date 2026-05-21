package kz.ablaysharimov.taskmanagementsystem.exception;

public class AblaySharimovUnauthorizedException extends RuntimeException {
    public AblaySharimovUnauthorizedException(String message) {
        super(message);
    }

    public AblaySharimovUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}

