package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output.strategies;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;

class LoanScoringTransportStrategyTest {

    @Test
    void buildScoreGenerationPayload_ShouldReturnScoringGenerationDTO() {
        LoanScoringTransportStrategy strategy = new LoanScoringTransportStrategy();
        
        ScoringGenerationRequest request = new ScoringGenerationRequest();
        request.setRequestType("PRESTAMO");
        request.setLoanAmount(15000.0);
        request.setTermMonths(60);
        // ... fill other basic fields if necessary for mapper ...

        Object result = strategy.buildScoreGenerationPayload(request);

        assertNotNull(result);
        assertTrue(result instanceof ScoringGenerationDTO);
        
        ScoringGenerationDTO dto = (ScoringGenerationDTO) result;
        assertEquals("PRESTAMO", dto.getRequestType());
        assertEquals(15000.0, dto.getLoanAmount());
        assertEquals(60, dto.getTermMonths());
    }
}
