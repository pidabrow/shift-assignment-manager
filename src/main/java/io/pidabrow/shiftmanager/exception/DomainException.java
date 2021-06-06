package io.pidabrow.shiftmanager.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DomainException extends RuntimeException {

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
