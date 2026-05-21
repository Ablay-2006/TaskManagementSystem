package kz.ablaysharimov.taskmanagementsystem.exception;

public class AblaySharimovBadRequestException extends RuntimeException {
    public AblaySharimovBadRequestException(String message) {
        super(message);
    }

    public AblaySharimovBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

