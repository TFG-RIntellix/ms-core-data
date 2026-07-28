package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input.exceptions;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.NotArchivedException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.DuplicateSimulationNameException;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Global exception handler for REST controllers.
 * Intercepts exceptions thrown across the application and formats them
 * into a standardized ApiErrorResponse payload.
 *
 * @author Lucía Fernández Mancebo
 * @date 28/07/2026
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        log.warn(LogMessage.EXCEPTION_UNEXPECTED, ex.getMessage());
        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn(LogMessage.EXCEPTION_ILLEGAL_ARGUMENT, ex.getMessage());
        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException thrown by the controller layer.
     *
     * @param ex      the exception instance containing validation errors
     * @param request the HttpServletRequest
     * @return ResponseEntity with the error details and appropriate HTTP status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        String message = String.join("; ", fieldErrors);
        log.warn(LogMessage.CONTROLLER_VALIDATION_ERROR, message);
        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles NotArchivedException thrown when an operation is attempted on a
     * simulation that is not archived.
     *
     * @param ex      the exception instance
     * @param request the HttpServletRequest
     * @return ResponseEntity with the error details and appropriate HTTP status
     */
    @ExceptionHandler(NotArchivedException.class)
    public ResponseEntity<ApiErrorResponse> handleNotArchivedException(NotArchivedException ex, HttpServletRequest request) {
        log.warn(LogMessage.EXCEPTION_ILLEGAL_ARGUMENT, ex.getMessage());
        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles DuplicateSimulationNameException thrown when attempting to create a simulation
     * with a name that already exists for the given request.
     *
     * @param ex      the exception instance
     * @param request the HttpServletRequest
     * @return ResponseEntity with the error details and appropriate HTTP status
     */
    @ExceptionHandler(DuplicateSimulationNameException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateSimulationNameException(DuplicateSimulationNameException ex, HttpServletRequest request) {
        log.warn(LogMessage.SERVICE_SIMULATION_ALREADY_EXISTS, ex.getMessage());
        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error(LogMessage.EXCEPTION_UNEXPECTED, ex.getMessage(), ex);
        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(LogMessage.API_ERROR_UNEXPECTED_MESSAGE)
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
