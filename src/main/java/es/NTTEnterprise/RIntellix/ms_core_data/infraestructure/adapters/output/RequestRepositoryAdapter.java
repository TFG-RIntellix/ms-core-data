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
        return requestRepository.findAll().stream()
                .map(requestMapper::toDomain)
                .toList();
    }

    @Override
    public Request findById(String requestId) throws EntityNotFoundException, IllegalArgumentException {
        Optional<RequestEntity> requestEntityOpt = requestRepository.findById(requestId);
        if (requestEntityOpt.isEmpty()) {
            throw new EntityNotFoundException("Request with ID " + requestId + " not found");
        }
        return requestMapper.toDomain(requestEntityOpt.get());
    }

    @Override
    public List<Request> findByPartyName(String partyName) {
        return requestRepository.findByPartyName(partyName).stream()
                .map(requestMapper::toDomain)
                .toList();
    }
    
    @Override
    public List<Request> findByStatus(String status) {
        return requestRepository.findByStatus(status).stream()
                .map(requestMapper::toDomain)
                .toList();
    }
}
