package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestDetailsDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestSummaryDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.RequestPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;

/**
 * This class implements the RequestPortService interface and provides the implementation of the methods defined in the interface. 
 * It is responsible for handling the business logic related to requests, such as listing requests and getting request details.
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
@Service
public class RequestApplicationService implements RequestPortService {

    private final RequestPortRepository requestPortRepository;
    private final RequestSummaryDTOMapper requestSummaryDTOMapper;
    private final RequestDetailsDTOMapper requestDetailsDTOMapper;

    public RequestApplicationService(RequestPortRepository requestPortRepository, 
                                     RequestSummaryDTOMapper requestSummaryDTOMapper,
                                     RequestDetailsDTOMapper requestDetailsDTOMapper) {
        this.requestPortRepository = requestPortRepository;
        this.requestSummaryDTOMapper = requestSummaryDTOMapper;
        this.requestDetailsDTOMapper = requestDetailsDTOMapper;
    }

    @Override
    public List<RequestSummaryDTO> listRequests(String partyName, String requestStatus) {
        // Fetch requests with filters applied at database level
        List<Request> requests;
        requests = requestPortRepository.findWithFilters(partyName, requestStatus);
        return requests.stream()
                .map(requestSummaryDTOMapper::toDTO)
                .toList();
    }

    @Override
    public RequestDetailsDTO getRequestDetails(String requestId)
            throws IllegalArgumentException, EntityNotFoundException {
        // Validate input
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("Request ID cannot be null or empty");
        }
        
        // Fetch the request from the repository
        Request request = requestPortRepository.findById(requestId);
        
        return requestDetailsDTOMapper.toDTO(request);
    }

}
