package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ScoringGenerationDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringGenerationPort;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Money;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.PropertyCollateral;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Person;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RequestDetails;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringPortRepository;

/**
 * Unit tests for {@link ScoringGenerationService}.
 * Covers duplicate checks, hard-cutoff rules, and model publishing.
 */
@DisplayName("ScoringGenerationService Tests")
@ExtendWith(MockitoExtension.class)
class ScoringGenerationServiceTest {

    @Mock
    private ScoringPortRepository scoringPortRepository;
    
    @Mock
    private PartyPortRepository partyPortRepository;
    
    @Mock
    private ScoringGenerationDTOMapper scoringGenerationMapper;
    
    @Mock
    private ScoringGenerationPort scoringGenerationPort;

    @Captor
    private ArgumentCaptor<Scoring> scoringCaptor;

    private ScoringGenerationService service;

    @BeforeEach
    void setUp() {
        service = new ScoringGenerationService(
            scoringPortRepository, partyPortRepository, scoringGenerationMapper, scoringGenerationPort);
    }

    @Test
    @DisplayName("Should skip generation if scoring already exists")
    void generateScoring_skipsIfAlreadyExists() throws EntityNotFoundException {
        Request request = new Request();
        request.setId("REQ-1");
        
        when(scoringPortRepository.findByRequestId("REQ-1")).thenReturn(new Scoring());

        service.generateScoring(request);

        verify(scoringPortRepository).findByRequestId("REQ-1");
        verifyNoInteractions(scoringGenerationPort, scoringGenerationMapper);
    }

    @Test
    @DisplayName("Should publish scoring request when no hard cutoffs trigger")
    void generateScoring_publishesSuccessfully() throws EntityNotFoundException {
        Request request = buildRequest(RequestType.PRESTAMO);
        request.setId("REQ-2");
        Party party = buildParty(0.3); // Safe DTI
        request.setParty(party);

        when(scoringPortRepository.findByRequestId("REQ-2")).thenThrow(new EntityNotFoundException(""));
        
        ScoringGenerationRequest payload = new ScoringGenerationRequest();
        when(scoringGenerationMapper.toOutputDTO(request, party)).thenReturn(payload);

        service.generateScoring(request);

        verify(scoringGenerationPort).publishScoringGenerationRequest(payload);
        verify(scoringPortRepository, never()).save(any(Scoring.class));
    }

    @Test
    @DisplayName("Should apply DTI hard cutoff for all types")
    void generateScoring_hardCutoff_DTI() throws EntityNotFoundException {
        Request request = buildRequest(RequestType.PRESTAMO);
        request.setId("REQ-3");
        Party party = buildParty(0.6); // DTI > 0.50
        request.setParty(party);

        when(scoringPortRepository.findByRequestId("REQ-3")).thenThrow(new EntityNotFoundException(""));

        service.generateScoring(request);

        verify(scoringPortRepository).save(scoringCaptor.capture());
        Scoring saved = scoringCaptor.getValue();
        assertEquals(1.0, saved.getResults().getProbabilityOfDefault());
        assertEquals("HIGH", saved.getResults().getRiskLevel());
        verifyNoInteractions(scoringGenerationPort);
    }

    @Test
    @DisplayName("Should apply LTV hard cutoff for mortgages")
    void generateScoring_hardCutoff_LTV() throws EntityNotFoundException {
        Request request = buildRequest(RequestType.HIPOTECA);
        request.setId("REQ-4");
        // Loan 90k, Property 100k -> LTV 0.90 (> 0.80)
        request.getRequestDetails().setRequestedAmount(new Money(90000.0, "EUR"));
        PropertyCollateral collateral = new PropertyCollateral(new Money(100000.0, "EUR"), true);
        request.setCollateral(collateral);

        Party party = buildParty(0.3);
        request.setParty(party);

        when(scoringPortRepository.findByRequestId("REQ-4")).thenThrow(new EntityNotFoundException(""));

        service.generateScoring(request);

        verify(scoringPortRepository).save(scoringCaptor.capture());
        Scoring saved = scoringCaptor.getValue();
        assertEquals(1.0, saved.getResults().getProbabilityOfDefault());
        verifyNoInteractions(scoringGenerationPort);
    }

    private Request buildRequest(RequestType type) {
        Request request = new Request();
        RequestDetails details;
        if (type == RequestType.TARJETA_CREDITO) {
            details = new RequestDetails(type, es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose.OTROS, new Money(0.0, "EUR"), 0, 0.0, new Money(1000.0, "EUR"), false, "FIXED", "Personal");
        } else {
            details = new RequestDetails(type, es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose.OTROS, new Money(10000.0, "EUR"), 12, 5.0, null, false, "FIXED", "Personal");
        }
        request.setRequestDetails(details);
        return request;
    }

    private Party buildParty(double dti) {
        Party party = new Party();
        Person pd = mock(Person.class);
        lenient().when(pd.getGlobalDTI()).thenReturn(dti);
        party.setPersonDetails(pd);
        return party;
    }
}
