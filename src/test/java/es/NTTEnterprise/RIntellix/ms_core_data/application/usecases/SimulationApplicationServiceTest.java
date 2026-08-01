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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ArchiveSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CalculatedSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.SimulatedResultsInputDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.DeltaInputDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.PageResponseDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.SimulationDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.PagedResult;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.DuplicateSimulationNameException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.NotArchivedException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.RequestPartyMismatchException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.SimulationPortRepository;

@DisplayName("SimulationApplicationService Tests")
@ExtendWith(MockitoExtension.class)
class SimulationApplicationServiceTest {

    @Mock
    private SimulationPortRepository simulationPortRepository;
    @Mock
    private PartyPortRepository partyPortRepository;
    @Mock
    private ScoringPortRepository scoringPortRepository;
    @Mock
    private RequestPortRepository requestPortRepository;
    @Mock
    private SimulationDTOMapper simulationDTOMapper;

    @Captor
    private ArgumentCaptor<Simulation> simulationCaptor;

    private SimulationApplicationService service;

    @BeforeEach
    void setUp() {
        service = new SimulationApplicationService(
            simulationPortRepository, partyPortRepository, scoringPortRepository,
            requestPortRepository, simulationDTOMapper);
    }

    // --- listSimulations ---

    @Test
    @DisplayName("Should list simulations resolving party names without search string")
    void listSimulations_success_noSearch() {
        Simulation sim = new Simulation();
        sim.setPartyId("P-1");
        sim.setRequestId("REQ-1");
        
        PagedResult<Simulation> pagedResult = new PagedResult<>(List.of(sim), 1, 1, 0, 10);
        
        when(simulationPortRepository.findWithFilters(null, null, null, false, 0, 10, "simulationDate", "desc")).thenReturn(pagedResult);
        when(partyPortRepository.findPartyNames(Set.of("P-1"))).thenReturn(Map.of("P-1", new Party()));
        
        Request req = new Request();
        req.setRequestCode("REQ-CODE-1");
        when(requestPortRepository.findRequestsByIds(anySet())).thenReturn(Map.of("REQ-1", req));
                
        when(simulationDTOMapper.toSummaryDTO(sim)).thenReturn(new SimulationSummaryDTO());

        PageResponseDTO<SimulationSummaryDTO> results = service.listSimulations(null, false, 0, 10, "simulationDate", "desc");
        assertEquals(1, results.getContent().size());
        verify(partyPortRepository, never()).findPartyIdsByNameMatch(anyString());
        verify(requestPortRepository, never()).findRequestIdsBySearch(anyString());
    }

    @Test
    @DisplayName("Should list simulations resolving party names with search string")
    void listSimulations_success_withSearch() {
        Simulation sim = new Simulation();
        sim.setPartyId("P-1");
        sim.setRequestId("REQ-1");

        PagedResult<Simulation> pagedResult = new PagedResult<>(List.of(sim), 1, 1, 0, 10);

        when(partyPortRepository.findPartyIdsByNameMatch("test")).thenReturn(Set.of("P-1"));
        when(requestPortRepository.findRequestIdsBySearch("test")).thenReturn(List.of("REQ-1"));
        when(simulationPortRepository.findWithFilters("test", List.of("P-1"), List.of("REQ-1"), true, 0, 10, "simulationDate", "desc")).thenReturn(pagedResult);
        when(partyPortRepository.findPartyNames(Set.of("P-1"))).thenReturn(Map.of("P-1", new Party()));
        when(requestPortRepository.findRequestsByIds(anySet())).thenReturn(Map.of());
                
        when(simulationDTOMapper.toSummaryDTO(sim)).thenReturn(new SimulationSummaryDTO());

        PageResponseDTO<SimulationSummaryDTO> results = service.listSimulations("test", true, 0, 10, "simulationDate", "desc");
        assertEquals(1, results.getContent().size());
    }

    // --- getSimulationDetails ---

    @Test
    @DisplayName("Should get simulation details successfully")
    void getSimulationDetails_success() throws EntityNotFoundException {
        Simulation sim = new Simulation();
        sim.setBaseScoringId("SCORE-1");
        Scoring baseScoring = new Scoring();

        when(simulationPortRepository.findById("SIM-1")).thenReturn(sim);
        when(scoringPortRepository.findById("SCORE-1")).thenReturn(baseScoring);
        when(simulationDTOMapper.toDetailsDTO(sim, baseScoring)).thenReturn(new SimulationDetailsDTO());

        SimulationDetailsDTO result = service.getSimulationDetails("SIM-1");
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should get simulation details when base scoring is missing")
    void getSimulationDetails_baseScoringMissing() throws EntityNotFoundException {
        Simulation sim = new Simulation();
        sim.setBaseScoringId("SCORE-1");

        when(simulationPortRepository.findById("SIM-1")).thenReturn(sim);
        when(scoringPortRepository.findById("SCORE-1")).thenThrow(new EntityNotFoundException("Not found"));
        when(simulationDTOMapper.toDetailsDTO(sim, null)).thenReturn(new SimulationDetailsDTO());

        SimulationDetailsDTO result = service.getSimulationDetails("SIM-1");
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should throw when getSimulationDetails receives invalid ID")
    void getSimulationDetails_invalidId() {
        assertThrows(IllegalArgumentException.class, () -> service.getSimulationDetails(null));
        assertThrows(IllegalArgumentException.class, () -> service.getSimulationDetails("   "));
    }

    // --- createSimulation ---

    @Test
    @DisplayName("Should create simulation with generated name if null")
    void createSimulation_generatesName() {
        CreateSimulationDTO dto = new CreateSimulationDTO();
        dto.setRequestId("REQ-1");
        dto.setPartyId("P-1");
        
        Request req = new Request();
        req.setPartyId("P-1"); // Matches
        
        when(requestPortRepository.findById("REQ-1")).thenReturn(req);
        when(simulationPortRepository.existsByRequestIdAndScenarioName(eq("REQ-1"), anyString())).thenReturn(false);
        
        Simulation saved = new Simulation();
        saved.setId("SIM-1");
        when(simulationPortRepository.save(any(Simulation.class))).thenReturn(saved);

        String id = service.createSimulation(dto);

        assertEquals("SIM-1", id);
        assertNotNull(dto.getScenarioName()); // Generated
        verify(simulationPortRepository).save(any(Simulation.class));
    }

    @Test
    @DisplayName("Should create simulation with full results and delta")
    void createSimulation_fullResultsAndDelta() {
        CreateSimulationDTO dto = new CreateSimulationDTO();
        dto.setRequestId("REQ-1");
        dto.setPartyId("P-1");
        dto.setScenarioName("Custom");
        
        SimulatedResultsInputDTO results = new SimulatedResultsInputDTO();
        results.setDti(0.5);
        results.setMonthlyPayment(500.0);
        dto.setSimulatedResults(results);

        DeltaInputDTO delta = new DeltaInputDTO();
        delta.setDtiChange(0.1);
        dto.setDelta(delta);

        Request req = new Request();
        req.setPartyId("P-1");
        when(requestPortRepository.findById("REQ-1")).thenReturn(req);
        when(simulationPortRepository.existsByRequestIdAndScenarioName("REQ-1", "Custom")).thenReturn(false);
        
        Simulation saved = new Simulation();
        saved.setId("SIM-1");
        when(simulationPortRepository.save(simulationCaptor.capture())).thenReturn(saved);

        service.createSimulation(dto);

        Simulation captured = simulationCaptor.getValue();
        assertNotNull(captured.getSimulatedResults().getFinancialMetrics());
        assertEquals(0.5, captured.getSimulatedResults().getFinancialMetrics().getDebtToIncomeRatio());
        assertEquals(0.1, captured.getDtiChange());
    }

    @Test
    @DisplayName("Should throw DuplicateSimulationNameException if name exists")
    void createSimulation_duplicateName() {
        CreateSimulationDTO dto = new CreateSimulationDTO();
        dto.setRequestId("REQ-1");
        dto.setPartyId("P-1");
        dto.setScenarioName("Custom");
        
        Request req = new Request();
        req.setPartyId("P-1");
        when(requestPortRepository.findById("REQ-1")).thenReturn(req);
        when(simulationPortRepository.existsByRequestIdAndScenarioName("REQ-1", "Custom")).thenReturn(true);

        assertThrows(DuplicateSimulationNameException.class, () -> service.createSimulation(dto));
    }

    @Test
    @DisplayName("Should throw mismatch exception if party does not match request")
    void createSimulation_partyMismatch() {
        CreateSimulationDTO dto = new CreateSimulationDTO();
        dto.setRequestId("REQ-1");
        dto.setPartyId("P-2"); // Mismatch
        
        Request req = new Request();
        req.setPartyId("P-1");
        when(requestPortRepository.findById("REQ-1")).thenReturn(req);

        assertThrows(RequestPartyMismatchException.class, () -> service.createSimulation(dto));
    }

    // --- updateSimulationTemplate ---

    @Test
    @DisplayName("Should update simulation template successfully")
    void updateSimulationTemplate_success() throws EntityNotFoundException {
        Simulation sim = new Simulation();
        CalculatedSimulationDTO dto = new CalculatedSimulationDTO();
        
        SimulatedResultsInputDTO results = new SimulatedResultsInputDTO();
        results.setRiskGrade("A");
        dto.setSimulatedResults(results);

        DeltaInputDTO delta = new DeltaInputDTO();
        delta.setPdChange(0.01);
        dto.setDelta(delta);

        when(simulationPortRepository.findById("SIM-1")).thenReturn(sim);

        service.updateSimulationTemplate("SIM-1", dto);

        verify(simulationPortRepository).save(simulationCaptor.capture());
        Simulation captured = simulationCaptor.getValue();
        assertEquals("A", captured.getSimulatedResults().getRiskLevel()); // getRiskLevel
        assertEquals(0.01, captured.getPdChange());
    }

    @Test
    @DisplayName("Should throw when updateSimulationTemplate receives invalid ID")
    void updateSimulationTemplate_invalidId() {
        assertThrows(IllegalArgumentException.class, () -> service.updateSimulationTemplate(null, new CalculatedSimulationDTO()));
        assertThrows(IllegalArgumentException.class, () -> service.updateSimulationTemplate("  ", new CalculatedSimulationDTO()));
    }

    // --- archiveSimulation ---

    @Test
    @DisplayName("Should archive simulation successfully")
    void archiveSimulation_success() throws EntityNotFoundException {
        Simulation sim = new Simulation();
        sim.setArchived(false);
        
        ArchiveSimulationDTO dto = new ArchiveSimulationDTO();
        dto.setIsArchived(true);
        
        when(simulationPortRepository.findById("SIM-1")).thenReturn(sim);

        service.archiveSimulation("SIM-1", dto);

        assertTrue(sim.isArchived());
        verify(simulationPortRepository).save(sim);
    }

    @Test
    @DisplayName("Should throw when archiveSimulation receives invalid ID")
    void archiveSimulation_invalidId() {
        assertThrows(IllegalArgumentException.class, () -> service.archiveSimulation(null, new ArchiveSimulationDTO()));
        assertThrows(IllegalArgumentException.class, () -> service.archiveSimulation("  ", new ArchiveSimulationDTO()));
    }

    // --- deleteSimulation ---

    @Test
    @DisplayName("Should delete simulation successfully")
    void deleteSimulation_success() throws EntityNotFoundException, NotArchivedException {
        service.deleteSimulation("SIM-1");
        verify(simulationPortRepository).delete("SIM-1");
    }
}
