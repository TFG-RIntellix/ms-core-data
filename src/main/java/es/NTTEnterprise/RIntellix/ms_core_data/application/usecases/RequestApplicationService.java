package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestDetailsDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestSummaryDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Contract;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.RequestPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ContractPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * This class implements the RequestPortService interface and provides the
 * implementation of the methods defined in the interface.
 * It is responsible for handling the business logic related to requests, such
 * as listing requests and getting request details.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
@Slf4j
@Service
public class RequestApplicationService implements RequestPortService {

    private final RequestPortRepository requestPortRepository;
    private final PartyPortRepository partyPortRepository;
    private final ContractPortRepository contractPortRepository;
    private final RequestSummaryDTOMapper requestSummaryDTOMapper;
    private final RequestDetailsDTOMapper requestDetailsDTOMapper;

    public RequestApplicationService(RequestPortRepository requestPortRepository,
            PartyPortRepository partyPortRepository,
            ContractPortRepository contractPortRepository,
            RequestSummaryDTOMapper requestSummaryDTOMapper,
            RequestDetailsDTOMapper requestDetailsDTOMapper) {
        this.requestPortRepository = requestPortRepository;
        this.partyPortRepository = partyPortRepository;
        this.contractPortRepository = contractPortRepository;
        this.requestSummaryDTOMapper = requestSummaryDTOMapper;
        this.requestDetailsDTOMapper = requestDetailsDTOMapper;
    }

    @Override
    public List<RequestSummaryDTO> listRequests(String partyName, String requestStatus) {
        log.debug(LogMessage.SERVICE_LIST_REQUESTS_START, partyName, requestStatus);

        List<Request> requests = requestPortRepository.findWithFilters(partyName, requestStatus);
        log.debug(LogMessage.SERVICE_LIST_REQUESTS_RESULT, requests.size());

        // Obtain partyName for each request arrending to SPA principles.
        requests.forEach(request -> {
            if (request.getPartyId() != null) {
                request.setParty(partyPortRepository.findPartyName(request.getPartyId()));
            }
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
            throw new IllegalArgumentException("Request ID cannot be null or empty");
        }

        Request request = requestPortRepository.findById(requestId);
        log.debug(LogMessage.SERVICE_GET_DETAILS_FOUND, requestId);

        // Resolve full Party for detailed view (orchestration at application layer)
        if (request.getPartyId() != null) {
            Party party = partyPortRepository.findById(request.getPartyId());

            // Load active contracts and assign to Party
            List<Contract> activeContracts = contractPortRepository.findActiveByPartyId(request.getPartyId());
            log.debug(LogMessage.REPOSITORY_PARTY_CONTRACTS_LOADED, request.getPartyId(), activeContracts.size());

            if (party.getPersonDetails() != null) {
                party.getPersonDetails().setActiveContracts(activeContracts);
            }

            // TODO: This is a proof to make sure that the PersonDetails are fully loaded
            // before mapping to DTO, we should remove this in the future or move it to a
            // more appropriate place.
            party.getPersonDetails().getGlobalDTI();
            party.getPersonDetails().getTotalDebt();

            request.setParty(party);
        }

        RequestDetailsDTO result = requestDetailsDTOMapper.toDTO(request);

        log.debug(LogMessage.SERVICE_GET_DETAILS_COMPLETE, requestId);
        return result;
    }

    /**
     * Helper method to filter requests by party name. This is used when
     * the repository
     * does not support filtering by party name directly,
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
