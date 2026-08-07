package es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions;

/**
 * Exception thrown when attempting an invalid status transition on a request.
 * For example, transitioning from REVISADO to PENDIENTE_DE_REVISION is not allowed.
 *
 * @author Lucía Fernández Mancebo
 * @date 05/08/2026
 */
public class InvalidStatusTransitionException extends RuntimeException {

    /**
     * Constructs a new InvalidStatusTransitionException with the specified detail message.
     *
     * @param message the detail message providing context about the invalid transition
     */
    public InvalidStatusTransitionException(String message) {
        super(message);
    }

}
