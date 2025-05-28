package com.jefferson.geradorhorarios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Mapeia esta exceção para o status HTTP 404 Not Found
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {

        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long resourceId) {
        super(String.format("%s not found with ID: %d", resourceName, resourceId));
    }

    public ResourceNotFoundException(String resourceName, Throwable cause) {
        super(resourceName, cause);
    }
}
