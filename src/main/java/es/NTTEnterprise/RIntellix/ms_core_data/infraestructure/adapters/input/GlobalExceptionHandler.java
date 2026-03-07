package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.input;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Centralized exception handler for all REST controllers.
 * Translates domain and validation exceptions into consistent HTTP error
 * responses, avoiding duplicated try-catch blocks in every controller method.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-07-2026
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles EntityNotFoundException thrown by service or repository layers.
     * 
     * @param ex      the exception instance containing details about the not found
     *                entity
     * @param request the HttpServletRequest to extract request details for logging
     *                and response
     * @return ResponseEntity with the error details and appropriate HTTP status
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex,
            HttpServletRequest request) {
        log.warn(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handles validation errors thrown by the controller layer.
     * 
     * @param ex      the exception instance containing validation errors
     * @param request the HttpServletRequest to extract request details for logging
     *                and response
     * @return ResponseEntity with the error details and appropriate HTTP status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        String message = String.join("; ", fieldErrors);
        log.warn(LogMessage.CONTROLLER_VALIDATION_ERROR, message);
        log.warn(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.BAD_REQUEST.value(), message);
        return buildResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    /**
     * Handles IllegalArgumentException thrown by service or controller layers when
     * method arguments are invalid or when invalid IDs are provided.
     * 
     * @param ex      the exception instance containing details about the illegal
     *                argument
     * @param request the HttpServletRequest to extract request details for logging
     *                and response
     * @return ResponseEntity with the error details and appropriate HTTP status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex,
            HttpServletRequest request) {
        log.warn(LogMessage.CONTROLLER_VALIDATION_ERROR, ex.getMessage());
        log.warn(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handles any uncaught exceptions that occur during request processing,
     * returning a
     * generic 500 Internal Server Error response. This ensures that unexpected
     * errors
     * are logged and that the client receives a consistent error response format,
     * even in
     * cases of unhandled exceptions.
     * 
     * @param ex      the exception instance containing details about the unexpected
     *                error
     * @param request the HttpServletRequest to extract request details for logging
     *                and response
     * @return ResponseEntity with the error details and appropriate HTTP status
     * 
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error(LogMessage.CONTROLLER_UNEXPECTED_ERROR, ex.getMessage(), ex);
        log.error(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error");
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request.getRequestURI());
    }

    /**
     * Utility method to build a consistent error response body with a timestamp,
     * status,
     * error message, and request path. This method is used by all exception
     * handlers
     * to ensure that error responses have a uniform structure across the
     * application.
     * 
     * @param status  the HTTP status to be returned in the response
     * @param message the error message to be included in the response body
     * @param path    the request URI that caused the error, included for client
     *                reference
     * @return ResponseEntity with the constructed error body and specified HTTP
     *         status
     * 
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);
        return ResponseEntity.status(status).body(body);
    }
}
