package es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions;

/**
 * Custom exception thrown when an operation is attempted on a simulation that
 * is not archived.
 * This exception is used to enforce that certain operations (like retrieval of
 * archived simulations) can only be performed on simulations that have been
 * marked as archived.
 * 
 * @author Lucía Fernández Mancebo
 * @date 07/03/2026
 */
public class NotArchivedException extends RuntimeException {

    /**
     * Constructs a new NotArchivedException with the specified detail message.
     * 
     * @param message the detail message to be included in the exception, providing
     *                context about the error
     */
    public NotArchivedException(String message) {
        super(message);
    }

}
