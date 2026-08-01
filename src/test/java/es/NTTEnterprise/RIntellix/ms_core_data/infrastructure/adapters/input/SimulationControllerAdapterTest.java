package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input.exceptions.GlobalExceptionHandler;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.DeltaInputDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.SimulatedResultsInputDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.PageResponseDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.SimulationPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.DuplicateSimulationNameException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.RequestPartyMismatchException;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;

@DisplayName("SimulationControllerAdapter Tests")
@ExtendWith(MockitoExtension.class)
class SimulationControllerAdapterTest {

    private MockMvc mockMvc;

    @Mock
    private SimulationPortService simulationPortService;

    @InjectMocks
    private SimulationControllerAdapter controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()) // Assuming a GlobalExceptionHandler exists
                .build();
    }

    @Test
    @DisplayName("GET /api/simulations should return 200 with list of simulations")
    void testListSimulations() throws Exception {
        PageResponseDTO<SimulationSummaryDTO> response = new PageResponseDTO<>(List.of(new SimulationSummaryDTO()), 1, 1, 0, 10);
        
        when(simulationPortService.listSimulations(eq("term"), eq(true), eq(0), eq(10), eq("simulationDate"), eq("desc")))
            .thenReturn(response);

        mockMvc.perform(get("/api/simulations")
                .param("search", "term")
                .param("archived", "true")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "simulationDate")
                .param("sortDir", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @DisplayName("POST /api/simulations should return 400 when DuplicateSimulationNameException is thrown")
    void testCreateSimulation_DuplicateName() throws Exception {
        CreateSimulationDTO dto = new CreateSimulationDTO();
        dto.setRequestId("REQ-1");
        dto.setPartyId("PARTY-1");
        dto.setBaseScoringId("SCORE-1");
        dto.setFormChanges(java.util.Map.of("field", "value"));
        dto.setSimulatedResults(new SimulatedResultsInputDTO()); // Assuming empty constructor passes if nested fields aren't strictly validated, or we provide minimal. Let's just mock with lenient if needed, but if there are inner @NotNull we might fail.
        dto.setDelta(new DeltaInputDTO());
        dto.setScenarioName("ExistingName");

        lenient().when(simulationPortService.createSimulation(any(CreateSimulationDTO.class)))
            .thenThrow(new DuplicateSimulationNameException(LogMessage.EXCEPTION_DUPLICATE_SIMULATION_NAME));

        mockMvc.perform(post("/api/simulations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/simulations should return 400 when RequestPartyMismatchException is thrown")
    void testCreateSimulation_PartyMismatch() throws Exception {
        CreateSimulationDTO dto = new CreateSimulationDTO();
        dto.setRequestId("REQ-1");
        dto.setPartyId("PARTY-1");
        dto.setBaseScoringId("SCORE-1");
        dto.setFormChanges(java.util.Map.of("field", "value"));
        dto.setSimulatedResults(new SimulatedResultsInputDTO());
        dto.setDelta(new DeltaInputDTO());
        dto.setScenarioName("NewName");

        String errorMsg = String.format(LogMessage.EXCEPTION_REQUEST_PARTY_MISMATCH, "REQ-1", "PARTY-2", "PARTY-1");
        
        lenient().when(simulationPortService.createSimulation(any(CreateSimulationDTO.class)))
            .thenThrow(new RequestPartyMismatchException(errorMsg));

        mockMvc.perform(post("/api/simulations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
