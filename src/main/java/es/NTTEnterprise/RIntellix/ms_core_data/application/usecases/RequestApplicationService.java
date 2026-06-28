package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.List;
import java.util.Objects;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestDetailsDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestSummaryDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.RequestPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Application service implementing business logic for request operations.
 *
 * Provides listing and detail retrieval of requests with associated party
 * and contract information, triggering async scoring generation after details
 * fetch.
 *
 * Uses strategy pattern for request type-specific detail processing:
 * - Loans/Mortgages: Full business logic with DTI and debt calculations
 * - Credit Cards: Credit card-specific processing
 *
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
@Slf4j
public class RequestApplicationService implements RequestPortService {

    private final RequestPortRepository requestPortRepository;
    private final PartyPortRepository partyPortRepository;
    private final RequestSummaryDTOMapper requestSummaryDTOMapper;
    private final RequestDetailsDTOMapper requestDetailsDTOMapper;
    private final ScoringGenerationService scoringGenerationService;

    public RequestApplicationService(RequestPortRepository requestPortRepository,
            PartyPortRepository partyPortRepository,
            RequestSummaryDTOMapper requestSummaryDTOMapper,
            RequestDetailsDTOMapper requestDetailsDTOMapper,
            ScoringGenerationService scoringGenerationService) {
        this.requestPortRepository = Objects.requireNonNull(requestPortRepository);
        this.partyPortRepository = Objects.requireNonNull(partyPortRepository);
        this.requestSummaryDTOMapper = Objects.requireNonNull(requestSummaryDTOMapper);
        this.requestDetailsDTOMapper = Objects.requireNonNull(requestDetailsDTOMapper);
        this.scoringGenerationService = Objects.requireNonNull(scoringGenerationService);
    }

    @Override
    public List<RequestSummaryDTO> listRequests(String partyName, String partyId, String requestStatus) {
        log.debug(LogMessage.SERVICE_LIST_REQUESTS_START, partyName, partyId, requestStatus);

        List<Request> requests = requestPortRepository.findWithFilters(partyId, requestStatus);
        log.debug(LogMessage.SERVICE_LIST_REQUESTS_RESULT, requests.size());

        // Resolve party name for each request at application layer to keep SRP.
        requests.forEach(request -> {
            request.setParty(partyPortRepository.findPartyName(request.getPartyId()));
        });

        // Filter by name because repository does not support this direct filtering.
        requests = filterByPartyName(requests, partyName);

        log.debug(LogMessage.SERVICE_LIST_REQUESTS_MAPPING, requests.size());
        return requests.stream()
                .map(requestSummaryDTOMapper::toDTO)
                .toList();
    }

    @Override
    public RequestDetailsDTO getRequestDetails(String requestId)
            throws IllegalArgumentException, EntityNotFoundException {

        log.debug(LogMessage.SERVICE_GET_DETAILS_START, requestId);
        log.debug(LogMessage.SERVICE_GET_DETAILS_VALIDATION, requestId);

        if (requestId == null || requestId.isBlank()) {
            log.warn(LogMessage.SERVICE_GET_DETAILS_VALIDATION_ERROR);
            throw new IllegalArgumentException(LogMessage.EXCEPTION_INVALID_REQUEST_ID);
        }

        Request request = requestPortRepository.findById(requestId);
        log.debug(LogMessage.SERVICE_GET_DETAILS_FOUND, requestId);

        // Resolve full Party for detailed view (orchestration at application layer)
        Party party = partyPortRepository.findById(request.getPartyId());

        request.setParty(party);

        RequestDetailsDTO result = requestDetailsDTOMapper.toDTO(request);

        // Trigger asynchronous scoring generation after response is prepared
        log.debug(LogMessage.SERVICE_GET_DETAILS_TRIGGER_SCORING, requestId);
        scoringGenerationService.generateScoring(request);

        log.debug(LogMessage.SERVICE_GET_DETAILS_COMPLETE, requestId);
        return result;
    }

    /**
     * Helper method to filter requests by party name. This is used when
     * the repository does not support filtering by party name directly,
     * so we retrieve all requests and then filter in memory.
     *
     * @param requests  the list of requests to filter
     * @param partyName the party name to filter by
     * @return the filtered list of requests that match the party name
     */
    private List<Request> filterByPartyName(List<Request> requests, String partyName) {
        if (partyName == null || partyName.isBlank()) {
            return requests;
        }
        return requests.stream()
                .filter(request -> request.getParty().getPersonDetails().getFullName().contains(partyName))
                .toList();
    }

}
