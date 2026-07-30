package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.RequestEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers.RequestMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository.RequestRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Date;
import java.time.ZoneId;

/**
 * Adapter class that implements the RequestPortRepository interface.
 * Responsible ONLY for Request aggregate persistence operations.
 * Party resolution is handled at the application layer to respect SRP.
 * 
 * @author Lucía Fernández Mancebo
 * @date 28/02/2026
 */
@Slf4j
@Repository
public class RequestRepositoryAdapter implements RequestPortRepository {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public RequestRepositoryAdapter(RequestRepository requestRepository, RequestMapper requestMapper) {
        this.requestRepository = Objects.requireNonNull(requestRepository);
        this.requestMapper = Objects.requireNonNull(requestMapper);
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

        Optional<RequestEntity> requestEntityOpt = requestRepository.findById(new ObjectId(requestId));

        if (requestEntityOpt.isEmpty()) {
            log.warn(LogMessage.REPOSITORY_FIND_BY_ID_NOT_FOUND, requestId);
            throw new EntityNotFoundException(String.format(LogMessage.EXCEPTION_REQUEST_NOT_FOUND, requestId));
        }

        log.debug(LogMessage.REPOSITORY_FIND_BY_ID_FOUND, requestId);
        log.debug(LogMessage.REPOSITORY_FIND_BY_ID_MAPPING, requestId);

        return requestMapper.toDomain(requestEntityOpt.get());
    }

    @Override
    public List<Request> findWithFilters(String search, List<String> partyIds, String requestStatus) {
        log.debug(LogMessage.REPOSITORY_FIND_WITH_FILTERS_START, search, requestStatus);

        String searchParam = (search != null && !search.isBlank()) ? search : "";

        List<ObjectId> partyOids = List.of();
        if (partyIds != null && !partyIds.isEmpty()) {
            partyOids = partyIds.stream()
                    .filter(ObjectId::isValid)
                    .map(ObjectId::new)
                    .toList();
        }

        List<RequestEntity> entities = requestRepository.findWithFilters(searchParam, partyOids, requestStatus);
        log.debug(LogMessage.REPOSITORY_FIND_WITH_FILTERS_RESULT, entities.size());

        log.debug(LogMessage.REPOSITORY_FIND_WITH_FILTERS_MAPPING, entities.size());
        return entities.stream()
                .map(requestMapper::toDomain)
                .toList();
    }

    @Override
    public void updateReviewStatus(String requestId, RequestStatus status, Date lastReviewDate)
            throws EntityNotFoundException {
        log.debug(LogMessage.REPOSITORY_REQUEST_UPDATING_REVIEW_STATUS, requestId);
        Optional<RequestEntity> requestEntityOpt = requestRepository.findById(new ObjectId(requestId));
        if (requestEntityOpt.isEmpty()) {
            throw new EntityNotFoundException(String.format(LogMessage.EXCEPTION_REQUEST_NOT_FOUND, requestId));
        }
        RequestEntity entity = requestEntityOpt.get();
        entity.setStatus(status);
        if (lastReviewDate != null) {
            entity.setLastReviewDate(lastReviewDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        requestRepository.save(entity);
    }

    @Override
    public Map<String, Request> findRequestsByIds(Set<String> requestIds) {
        if (requestIds == null || requestIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<ObjectId> objectIds = requestIds.stream()
                .filter(ObjectId::isValid)
                .map(ObjectId::new)
                .toList();

        List<RequestEntity> entities = (List<RequestEntity>) requestRepository.findAllById(objectIds);

        return entities.stream()
                .map(requestMapper::toDomain)
                .collect(Collectors.toMap(Request::getId, req -> req));
    }

}
