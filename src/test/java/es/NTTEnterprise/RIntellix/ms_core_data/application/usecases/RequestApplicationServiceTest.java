package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestPartyDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestDetailsDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestPartyDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestSummaryDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;

/**
 * Unit tests for {@link RequestApplicationService}.
 * Covers request listing with filters, detailed fetching, party fetching, and async scoring triggers.
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
        when(requestPortRepository.findWithFilters(search, List.of("P-1"), status))
                .thenReturn(List.of(request));
                
        Party party = new Party();
        when(partyPortRepository.findPartyNames(Set.of("P-1"))).thenReturn(Map.of("P-1", party));
        
        RequestSummaryDTO summaryDTO = new RequestSummaryDTO();
        when(requestSummaryDTOMapper.toDTO(request)).thenReturn(summaryDTO);

        List<RequestSummaryDTO> results = service.listRequests(search, status);

        assertEquals(1, results.size());
        assertEquals(summaryDTO, results.get(0));
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
}
