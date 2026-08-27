package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output.strategies;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreditCardScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;

class CreditCardScoringTransportStrategyTest {

    @Test
    void buildScoreGenerationPayload_ShouldReturnCreditCardDTO() {
        CreditCardScoringTransportStrategy strategy = new CreditCardScoringTransportStrategy();
        
        ScoringGenerationRequest request = new ScoringGenerationRequest();
        request.setRequestType("TARJETA_CREDITO");
        request.setCreditLimit(5000.0);
        request.setIsRevolving(true);
        // ... fill other basic fields if necessary for mapper ...

        Object result = strategy.buildScoreGenerationPayload(request);

        assertNotNull(result);
        assertTrue(result instanceof CreditCardScoringGenerationDTO);
        
        CreditCardScoringGenerationDTO dto = (CreditCardScoringGenerationDTO) result;
        assertEquals("TARJETA_CREDITO", dto.getRequestType());
        assertEquals(5000.0, dto.getCreditLimit());
        assertTrue(dto.getIsRevolving());
    }
}
