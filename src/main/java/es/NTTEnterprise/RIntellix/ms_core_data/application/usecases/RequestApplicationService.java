package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestPartyDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestDetailsDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestPartyDTOMapper;
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
    private final RequestPartyDTOMapper requestPartyDTOMapper;
    private final ScoringGenerationService scoringGenerationService;

    public RequestApplicationService(RequestPortRepository requestPortRepository,
            PartyPortRepository partyPortRepository,
            RequestSummaryDTOMapper requestSummaryDTOMapper,
            RequestDetailsDTOMapper requestDetailsDTOMapper,
            RequestPartyDTOMapper requestPartyDTOMapper,
            ScoringGenerationService scoringGenerationService) {
        this.requestPortRepository = Objects.requireNonNull(requestPortRepository);
        this.partyPortRepository = Objects.requireNonNull(partyPortRepository);
        this.requestSummaryDTOMapper = Objects.requireNonNull(requestSummaryDTOMapper);
        this.requestDetailsDTOMapper = Objects.requireNonNull(requestDetailsDTOMapper);
        this.requestPartyDTOMapper = Objects.requireNonNull(requestPartyDTOMapper);
        this.scoringGenerationService = Objects.requireNonNull(scoringGenerationService);
    }

    @Override
    public List<RequestSummaryDTO> listRequests(String search, String requestStatus) {
        log.debug(LogMessage.SERVICE_LIST_REQUESTS_START, search, requestStatus);

        // 1. Resolve matching party IDs based on the search string
        Set<String> matchingPartyIds = null;
        if (search != null && !search.isBlank()) {
            matchingPartyIds = partyPortRepository.findPartyIdsByNameMatch(search);
        }

        // 2. Retrieve requests applying the generic search and party IDs
        List<String> partyIdsList = matchingPartyIds != null ? matchingPartyIds.stream().toList() : null;
        List<Request> requests = requestPortRepository.findWithFilters(search, partyIdsList, requestStatus);
        log.debug(LogMessage.SERVICE_LIST_REQUESTS_RESULT, requests.size());

        // Resolve party name for each request at application layer to keep SRP.
        Set<String> partyIds = requests.stream()
                .map(Request::getPartyId)
                .collect(Collectors.toSet());

        Map<String, Party> partyNames = partyPortRepository.findPartyNames(partyIds);

        requests.forEach(request -> {
            request.setParty(partyNames.get(request.getPartyId()));
        });

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

        validateRequestId(requestId);

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

    @Override
    public RequestPartyDTO getRequestParty(String requestId)
            throws IllegalArgumentException, EntityNotFoundException {

        log.debug(LogMessage.SERVICE_GET_PARTY_START, requestId);

        validateRequestId(requestId);

        Request request = requestPortRepository.findById(requestId);

        // Resolve only the party name (no full PII) for this internal projection.
        Party party = partyPortRepository.findPartyName(request.getPartyId());

        RequestPartyDTO result = requestPartyDTOMapper.toDTO(request, party);

        log.debug(LogMessage.SERVICE_GET_PARTY_COMPLETE, requestId);
        return result;
    }



    private void validateRequestId(String requestId) {
        if (requestId == null || requestId.isBlank()) {
            log.warn(LogMessage.SERVICE_GET_DETAILS_VALIDATION_ERROR);
            throw new IllegalArgumentException(LogMessage.EXCEPTION_INVALID_REQUEST_ID);
        }
    }

}
