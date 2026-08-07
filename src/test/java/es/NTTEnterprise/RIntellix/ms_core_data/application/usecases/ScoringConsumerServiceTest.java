package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.FinancialMetricsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.RiskResultsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringResultMessageDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.XAIFeatureDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringPortRepository;

@DisplayName("ScoringConsumerService Tests")
@ExtendWith(MockitoExtension.class)
class ScoringConsumerServiceTest {

    @Mock
    private ScoringPortRepository scoringPortRepository;

    @Captor
    private ArgumentCaptor<Scoring> scoringCaptor;

    private ScoringConsumerService service;

    @BeforeEach
    void setUp() {
        service = new ScoringConsumerService(scoringPortRepository);
    }

    @Test
    @DisplayName("Should process scoring message successfully")
    void processScoringMessage_success() {
        ScoringResultMessageDTO dto = new ScoringResultMessageDTO();
        dto.setRequestId("REQ-1");
        dto.setModelVersion("1.0");
        dto.setScoringDate(new Date()); // Fix setScoringDate

        RiskResultsDTO results = new RiskResultsDTO("0.05", 0.45, 10000.0, "200.0", "A");
        
        FinancialMetricsDTO fmDTO = new FinancialMetricsDTO();
        fmDTO.setDebtToIncomeRatio(0.3);
        results.setFinancialMetrics(fmDTO);
        
        dto.setResults(results);

        List<XAIFeatureDTO> contrib = new ArrayList<>();
        XAIFeatureDTO xai = new XAIFeatureDTO();
        xai.setFeatureName("feature1");
        xai.setShapValue(0.5);
        contrib.add(xai);
        dto.setExplainability(contrib);

        Scoring saved = new Scoring();
        saved.setId("SCORE-1");
        when(scoringPortRepository.save(any(Scoring.class))).thenReturn(saved);

        Scoring result = service.processScoringMessage(dto);

        assertNotNull(result);
        assertEquals("SCORE-1", result.getId());
        
        verify(scoringPortRepository).save(scoringCaptor.capture());
        Scoring captured = scoringCaptor.getValue();
        assertEquals("REQ-1", captured.getRequestId());
        assertEquals("1.0", captured.getModelVersion());
        assertEquals(0.05, captured.getResults().getProbabilityOfDefault());
        assertEquals(0.45, captured.getResults().getLossGivenDefault());
        assertNotNull(captured.getResults().getFinancialMetrics());
        assertEquals(0.3, captured.getResults().getFinancialMetrics().getDebtToIncomeRatio());
    }

    @Test
    @DisplayName("Should throw NullPointerException when DTO is null")
    void processScoringMessage_nullDto() {
        NullPointerException exception = assertThrows(NullPointerException.class, 
            () -> service.processScoringMessage(null));
        assertEquals("Scoring consumer message cannot be null", exception.getMessage());
        
        verifyNoInteractions(scoringPortRepository);
    }
}
