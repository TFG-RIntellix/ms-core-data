package es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions;

/**
 * Exception thrown when a scoring message fails validation against the schema
 * or business rules.
 * This exception indicates a permanent validation failure that should not be
 * retried.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
public class ScoringValidationException extends RuntimeException {

    /**
     * Constructs a ScoringValidationException with a detail message.
     * 
     * @param message the detail message explaining the validation failure
     */
    public ScoringValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a ScoringValidationException with a detail message and cause.
     * 
     * @param message the detail message explaining the validation failure
     * @param cause   the cause of the validation failure
     */
    public ScoringValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
