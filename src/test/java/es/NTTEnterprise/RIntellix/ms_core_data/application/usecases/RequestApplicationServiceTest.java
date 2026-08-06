package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.PageResponseDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestPartyDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestDetailsDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestPartyDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestSummaryDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.PagedResult;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.InvalidStatusTransitionException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;

/**
 * Unit tests for {@link RequestApplicationService}.
 * Covers request listing with filters, detailed fetching, party fetching, and
 * async scoring triggers.
 */
@DisplayName("RequestApplicationService Tests")
@ExtendWith(MockitoExtension.class)
class RequestApplicationServiceTest {

    @Mock
    private RequestPortRepository requestPortRepository;

    @Mock
    private PartyPortRepository partyPortRepository;

    @Mock
    private RequestSummaryDTOMapper requestSummaryDTOMapper;

    @Mock
    private RequestDetailsDTOMapper requestDetailsDTOMapper;

    @Mock
    private RequestPartyDTOMapper requestPartyDTOMapper;

    @Mock
    private ScoringGenerationService scoringGenerationService;

    private RequestApplicationService service;

    @BeforeEach
    void setUp() {
        service = new RequestApplicationService(
                requestPortRepository, partyPortRepository, requestSummaryDTOMapper,
                requestDetailsDTOMapper, requestPartyDTOMapper, scoringGenerationService);
    }

    @Test
    @DisplayName("Should list requests with search filters and resolve party names")
    void listRequests_success() {
        String search = "John";
        String status = "PENDING";

        when(partyPortRepository.findPartyIdsByNameMatch(search)).thenReturn(Set.of("P-1"));

        Request request = new Request();
        request.setPartyId("P-1");

        PagedResult<Request> pagedResult = new PagedResult<>(List.of(request), 1, 1, 0, 10);

        when(requestPortRepository.findWithFilters(search, List.of("P-1"), status, 0, 10, "creationDate", "desc"))
                .thenReturn(pagedResult);

        Party party = new Party();
        when(partyPortRepository.findPartyNames(Set.of("P-1"))).thenReturn(Map.of("P-1", party));

        RequestSummaryDTO summaryDTO = new RequestSummaryDTO();
        when(requestSummaryDTOMapper.toDTO(request)).thenReturn(summaryDTO);

        PageResponseDTO<RequestSummaryDTO> results = service.listRequests(search, status, 0, 10, "creationDate",
                "desc");

        assertEquals(1, results.getContent().size());
        assertEquals(summaryDTO, results.getContent().get(0));
        assertEquals(party, request.getParty()); // Verify party name was resolved
    }

    @Test
    @DisplayName("Should get request details, resolve full party and trigger scoring generation")
    void getRequestDetails_success() throws EntityNotFoundException {
        Request request = new Request();
        request.setPartyId("P-1");

        Party party = new Party();
        RequestDetailsDTO detailsDTO = new RequestDetailsDTO();

        when(requestPortRepository.findById("REQ-1")).thenReturn(request);
        when(partyPortRepository.findById("P-1")).thenReturn(party);
        when(requestDetailsDTOMapper.toDTO(request)).thenReturn(detailsDTO);

        RequestDetailsDTO result = service.getRequestDetails("REQ-1");

        assertEquals(detailsDTO, result);
        assertEquals(party, request.getParty());
        verify(scoringGenerationService).generateScoring(request); // Verify async trigger
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when request ID is null or blank")
    void getRequestDetails_nullId() {
        assertThrows(IllegalArgumentException.class, () -> service.getRequestDetails(null));
        assertThrows(IllegalArgumentException.class, () -> service.getRequestDetails("   "));
    }

    @Test
    @DisplayName("Should get request party for internal projection")
    void getRequestParty_success() throws EntityNotFoundException {
        Request request = new Request();
        request.setPartyId("P-1");

        Party party = new Party();
        RequestPartyDTO partyDTO = new RequestPartyDTO();

        when(requestPortRepository.findById("REQ-1")).thenReturn(request);
        when(partyPortRepository.findPartyName("P-1")).thenReturn(party);
        when(requestPartyDTOMapper.toDTO(request, party)).thenReturn(partyDTO);

        RequestPartyDTO result = service.getRequestParty("REQ-1");

        assertEquals(partyDTO, result);
        verify(partyPortRepository).findPartyName("P-1"); // Ensure minimal projection is fetched
    }

    @Test
    @DisplayName("Should update request status to REVISADO successfully and return details")
    void updateRequestStatus_success_returnsDetails() throws Exception {
        Request request = new Request();
        request.setPartyId("P-1");
        request.setRequestStatus(RequestStatus.PENDIENTE_DE_REVISION);

        Party party = new Party();
        RequestDetailsDTO detailsDTO = new RequestDetailsDTO();

        when(requestPortRepository.findById("REQ-1")).thenReturn(request);
        when(partyPortRepository.findById("P-1")).thenReturn(party);
        when(requestDetailsDTOMapper.toDTO(request)).thenReturn(detailsDTO);

        RequestDetailsDTO result = service.updateRequestStatus("REQ-1", "REVISADO");

        assertEquals(detailsDTO, result);
        verify(requestPortRepository).updateReviewStatus(eq("REQ-1"), eq(RequestStatus.REVISADO), any(Date.class));
    }

    @Test
    @DisplayName("Should throw InvalidStatusTransitionException when transition is invalid")
    void updateRequestStatus_invalidTransition_throwsException() {
        Request request = new Request();
        request.setRequestStatus(RequestStatus.REVISADO);

        when(requestPortRepository.findById("REQ-1")).thenReturn(request);

        assertThrows(InvalidStatusTransitionException.class, () -> service.updateRequestStatus("REQ-1", "REVISADO"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when status value is invalid")
    void updateRequestStatus_invalidStatusValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.updateRequestStatus("REQ-1", "INVALIDO"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when request ID is null")
    void updateRequestStatus_nullRequestId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.updateRequestStatus(null, "REVISADO"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when request ID is blank")
    void updateRequestStatus_blankRequestId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.updateRequestStatus("   ", "REVISADO"));
    }

    @Test
    @DisplayName("Should propagate EntityNotFoundException when request is not found")
    void updateRequestStatus_entityNotFound_throwsException() {
        when(requestPortRepository.findById("REQ-1")).thenThrow(new EntityNotFoundException("Not found"));

        assertThrows(EntityNotFoundException.class, () -> service.updateRequestStatus("REQ-1", "REVISADO"));
    }
}
