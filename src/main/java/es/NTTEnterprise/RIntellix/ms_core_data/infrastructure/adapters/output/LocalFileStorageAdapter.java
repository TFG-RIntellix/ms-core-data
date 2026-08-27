package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.FileStorageException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.FileStoragePort;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Infrastructure adapter that implements the FileStoragePort output port.
 * Retrieves files from the local file system using Spring's UrlResource.
 *
 * @author Lucía Fernández Mancebo
 * @date 16/08/2026
 */
@Slf4j
@Component
public class LocalFileStorageAdapter implements FileStoragePort {

    @Override
    public InputStream getFile(String filePath) throws FileStorageException {
        log.debug(LogMessage.FILE_STORAGE_GET_START, filePath);

        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                log.warn(LogMessage.FILE_STORAGE_GET_NOT_FOUND, filePath);
                throw new FileStorageException(String.format(LogMessage.EXCEPTION_FILE_NOT_FOUND, filePath));
            }

            log.debug(LogMessage.FILE_STORAGE_GET_COMPLETE, filePath);
            return resource.getInputStream();
        } catch (MalformedURLException e) {
            log.error(LogMessage.FILE_STORAGE_URL_ERROR, filePath, e);
            throw new FileStorageException(String.format(LogMessage.EXCEPTION_FILE_MALFORMED_URL, filePath), e);
        } catch (java.io.IOException e) {
            log.error(LogMessage.FILE_STORAGE_GET_NOT_FOUND, filePath, e);
            throw new FileStorageException(String.format(LogMessage.EXCEPTION_FILE_READ_ERROR, filePath), e);
        }

    }
}
