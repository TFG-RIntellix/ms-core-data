package es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions;

/**
 * Exception thrown when a file cannot be retrieved from the storage system.
 * This may occur when the file path is missing, the file does not exist,
 * or the file is not readable.
 *
 * @author Lucía Fernández Mancebo
 * @date 16/08/2026
 */
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
