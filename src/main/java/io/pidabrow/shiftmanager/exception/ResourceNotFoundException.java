package io.pidabrow.shiftmanager.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final String message;

    public ResourceNotFoundException(Class<?> resource, Long id) {
        this.message = String.format("Cannot find %s with id= %s", resource.getName(), id);
    }

    public ResourceNotFoundException(Class<?> resource) {
        this.message = String.format("Cannot find %s matching search criteria", resource.getName());
    }

    @Override
    public String getMessage() {
        return message;
    }
}
