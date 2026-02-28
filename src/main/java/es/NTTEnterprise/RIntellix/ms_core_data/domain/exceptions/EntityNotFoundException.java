package es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions;
/**
 * This class represents an exception that is thrown when an entity is not found in the database. It extends the RuntimeException class and takes a message as a parameter.
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
