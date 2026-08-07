package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.UpdateRequestStatusDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.RequestPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.InvalidStatusTransitionException;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input.exceptions.GlobalExceptionHandler;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;

@DisplayName("RequestControllerAdapter Tests")
@ExtendWith(MockitoExtension.class)
class RequestControllerAdapterTest {

    private MockMvc mockMvc;

    @Mock
    private RequestPortService requestPortService;

    @InjectMocks
    private RequestControllerAdapter controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("PUT /api/requests/{id} should return 200 with updated details when successful")
    void testUpdateRequestStatus_success() throws Exception {
        UpdateRequestStatusDTO dto = new UpdateRequestStatusDTO();
        dto.setRequestStatus("REVISADO");

        RequestDetailsDTO responseDTO = new RequestDetailsDTO();
        responseDTO.setRequestId("REQ-1");
        responseDTO.setStatus("REVISADO");

        when(requestPortService.updateRequestStatus("REQ-1", "REVISADO"))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/requests/REQ-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value("REQ-1"))
                .andExpect(jsonPath("$.status").value("REVISADO"));
    }

    @Test
    @DisplayName("PUT /api/requests/{id} should return 404 when request is not found")
    void testUpdateRequestStatus_notFound() throws Exception {
        UpdateRequestStatusDTO dto = new UpdateRequestStatusDTO();
        dto.setRequestStatus("REVISADO");

        when(requestPortService.updateRequestStatus("REQ-1", "REVISADO"))
                .thenThrow(new EntityNotFoundException(String.format(LogMessage.EXCEPTION_REQUEST_NOT_FOUND, "REQ-1")));

        mockMvc.perform(put("/api/requests/REQ-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/requests/{id} should return 409 when status transition is invalid")
    void testUpdateRequestStatus_invalidTransition() throws Exception {
        UpdateRequestStatusDTO dto = new UpdateRequestStatusDTO();
        dto.setRequestStatus("PENDIENTE_DE_REVISION");

        when(requestPortService.updateRequestStatus("REQ-1", "PENDIENTE_DE_REVISION"))
                .thenThrow(new InvalidStatusTransitionException(
                        String.format(LogMessage.EXCEPTION_INVALID_STATUS_TRANSITION, "REVISADO", "PENDIENTE_DE_REVISION")));

        mockMvc.perform(put("/api/requests/REQ-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /api/requests/{id} should return 400 when body is invalid (null status)")
    void testUpdateRequestStatus_nullStatus() throws Exception {
        UpdateRequestStatusDTO dto = new UpdateRequestStatusDTO();
        // requestStatus is null

        mockMvc.perform(put("/api/requests/REQ-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/requests/{id} should return 400 when status value is invalid (enum parse error)")
    void testUpdateRequestStatus_invalidStatusValue() throws Exception {
        UpdateRequestStatusDTO dto = new UpdateRequestStatusDTO();
        dto.setRequestStatus("INVALIDO");

        when(requestPortService.updateRequestStatus("REQ-1", "INVALIDO"))
                .thenThrow(new IllegalArgumentException(
                        String.format(LogMessage.EXCEPTION_INVALID_STATUS_VALUE, "INVALIDO")));

        mockMvc.perform(put("/api/requests/REQ-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
