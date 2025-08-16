package org.example.jakarthaeedemo.exception;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        Map<String, String> errors = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> pathOf(v),
                        ConstraintViolation::getMessage,
                        (a, b) -> a));
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("code", "VALIDATION_ERROR", "errors", errors))
                .build();
    }
    private String pathOf(ConstraintViolation<?> v) {
        return v.getPropertyPath() == null ? "" : v.getPropertyPath().toString();
    }
}
