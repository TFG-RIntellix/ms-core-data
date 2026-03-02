package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.RequestEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers.RequestMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository.RequestRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Adapter class that implements the RequestPortRepository interface.
 * Responsible ONLY for Request aggregate persistence operations.
 * Party resolution is handled at the application layer to respect SRP.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
@Slf4j
@Repository
public class RequestRepositoryAdapter implements RequestPortRepository {
    
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public RequestRepositoryAdapter(RequestRepository requestRepository, RequestMapper requestMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
    }


    @Override
    public List<Request> findAll() {
        log.debug(LogMessage.REPOSITORY_FIND_ALL_START);
        
        List<RequestEntity> entities = requestRepository.findAll();
        log.debug(LogMessage.REPOSITORY_FIND_ALL_RESULT, entities.size());
        
        return entities.stream()
                .map(requestMapper::toDomain)
                .toList();
    }

    @Override
    public Request findById(String requestId) throws EntityNotFoundException, IllegalArgumentException {
        log.debug(LogMessage.REPOSITORY_FIND_BY_ID_START, requestId);
        
        Optional<RequestEntity> requestEntityOpt = requestRepository.findById(requestId);
        
        if (requestEntityOpt.isEmpty()) {
            log.warn(LogMessage.REPOSITORY_FIND_BY_ID_NOT_FOUND, requestId);
            throw new EntityNotFoundException("Request with ID " + requestId + " not found");
        }
        
        log.debug(LogMessage.REPOSITORY_FIND_BY_ID_FOUND, requestId);
        log.debug(LogMessage.REPOSITORY_FIND_BY_ID_MAPPING, requestId);
        
        return requestMapper.toDomain(requestEntityOpt.get());
    }

    @Override
    public List<Request> findWithFilters(String partyName, String requestStatus) {
        log.debug(LogMessage.REPOSITORY_FIND_WITH_FILTERS_START, partyName, requestStatus);
        
        List<RequestEntity> entities = requestRepository.findWithFilters(partyName, requestStatus);
        log.debug(LogMessage.REPOSITORY_FIND_WITH_FILTERS_RESULT, entities.size());
        
        log.debug(LogMessage.REPOSITORY_FIND_WITH_FILTERS_MAPPING, entities.size());
        return entities.stream()
                .map(requestMapper::toDomain)
                .toList();
    }

}
