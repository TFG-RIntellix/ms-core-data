package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ScoringDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringPortRepository;

/**
 * Unit tests for {@link ScoringApplicationService}.
 * Covers retrieving scorings by request ID and input validation.
 */
@DisplayName("ScoringApplicationService Tests")
@ExtendWith(MockitoExtension.class)
class ScoringApplicationServiceTest {

    @Mock
    private ScoringPortRepository scoringPortRepository;
    
    @Mock
    private ScoringDTOMapper scoringDTOMapper;

    private ScoringApplicationService service;

    @BeforeEach
    void setUp() {
        service = new ScoringApplicationService(scoringPortRepository, scoringDTOMapper);
    }

    @Test
    @DisplayName("Should retrieve scoring by request ID")
    void getScoringByRequestId_success() throws EntityNotFoundException {
        Scoring scoring = new Scoring();
        ScoringDTO dto = new ScoringDTO();

        when(scoringPortRepository.findByRequestId("REQ-1")).thenReturn(scoring);
        when(scoringDTOMapper.toDTO(scoring)).thenReturn(dto);

        ScoringDTO result = service.getScoringByRequestId("REQ-1");

        assertEquals(dto, result);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException if scoring not found")
    void getScoringByRequestId_notFound() throws EntityNotFoundException {
        when(scoringPortRepository.findByRequestId("REQ-1")).thenThrow(new EntityNotFoundException("Not found"));

        assertThrows(EntityNotFoundException.class, () -> service.getScoringByRequestId("REQ-1"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if request ID is null or blank")
    void getScoringByRequestId_invalidId() {
        assertThrows(IllegalArgumentException.class, () -> service.getScoringByRequestId(null));
        assertThrows(IllegalArgumentException.class, () -> service.getScoringByRequestId("   "));
    }
}
