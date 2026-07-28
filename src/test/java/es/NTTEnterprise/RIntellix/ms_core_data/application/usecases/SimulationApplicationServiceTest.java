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

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ArchiveSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CalculatedSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.SimulationDTOMapper;
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

/**
 * Unit tests for {@link SimulationApplicationService}.
 * Covers listing, creating, archiving, updating templates, and party mismatches.
 */
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

    private SimulationApplicationService service;

    @BeforeEach
    void setUp() {
        service = new SimulationApplicationService(
            simulationPortRepository, partyPortRepository, scoringPortRepository,
            requestPortRepository, simulationDTOMapper);
    }

    @Test
    @DisplayName("Should list simulations resolving party names")
    void listSimulations_success() {
        Simulation sim = new Simulation();
        sim.setPartyId("P-1");
        
        when(simulationPortRepository.findWithFilters(null, null, false))
                .thenReturn(List.of(sim));
        when(partyPortRepository.findPartyNames(Set.of("P-1")))
                .thenReturn(Map.of("P-1", new Party()));
                
        SimulationSummaryDTO dto = new SimulationSummaryDTO();
        when(simulationDTOMapper.toSummaryDTO(sim)).thenReturn(dto);

        List<SimulationSummaryDTO> results = service.listSimulations(null, false);

        assertEquals(1, results.size());
        verify(partyPortRepository).findPartyNames(Set.of("P-1"));
    }

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
    @DisplayName("Should delete simulation successfully")
    void deleteSimulation_success() throws EntityNotFoundException, NotArchivedException {
        service.deleteSimulation("SIM-1");
        verify(simulationPortRepository).delete("SIM-1");
    }
}
