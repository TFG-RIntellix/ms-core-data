package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.ScoringPortService;

@ExtendWith(MockitoExtension.class)
class ScoringControllerAdapterTest {

    @Mock
    private ScoringPortService scoringPortService;

    private ScoringControllerAdapter controller;

    @BeforeEach
    void setUp() {
        controller = new ScoringControllerAdapter(scoringPortService);
    }

    @Test
    void getScoringByRequestId_ShouldReturnOkWithScoring() {
        String requestId = "req-123";
        ScoringDTO mockScoring = mock(ScoringDTO.class);
        
        when(scoringPortService.getScoringByRequestId(requestId)).thenReturn(mockScoring);

        ResponseEntity<ScoringDTO> response = controller.getScoringByRequestId(requestId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockScoring, response.getBody());
        
        verify(scoringPortService).getScoringByRequestId(requestId);
    }
}
