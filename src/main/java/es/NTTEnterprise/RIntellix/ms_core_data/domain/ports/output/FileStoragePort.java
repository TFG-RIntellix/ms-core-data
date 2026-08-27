package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output;

import java.io.InputStream;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.FileStorageException;

/**
 * Output port for file storage operations.
 * Abstracts the retrieval of files from the underlying storage system,
 * allowing the application layer to remain agnostic of the storage implementation.
 *
 * @author Lucía Fernández Mancebo
 * @date 16/08/2026
 */
public interface FileStoragePort {

    /**
     * Retrieves the content of a file as an InputStream.
     *
     * @param filePath the path to the file in the storage system
     * @return an InputStream to read the file content
     * @throws FileStorageException if the file does not exist, is not readable,
     *                              or the path is malformed
     */
    InputStream getFile(String filePath) throws FileStorageException;
}
