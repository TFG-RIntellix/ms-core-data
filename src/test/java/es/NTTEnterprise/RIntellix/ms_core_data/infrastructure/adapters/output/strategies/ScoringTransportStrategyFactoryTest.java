package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output.strategies;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringTransportStrategy;

class ScoringTransportStrategyFactoryTest {

    @Test
    void createStrategy_ShouldReturnLoanStrategy_ForPrestamo() {
        ScoringGenerationRequest req = mock(ScoringGenerationRequest.class);
        when(req.getRequestType()).thenReturn("PRESTAMO");

        ScoringTransportStrategy strategy = ScoringTransportStrategyFactory.createStrategy(req);
        
        assertTrue(strategy instanceof LoanScoringTransportStrategy);
    }

    @Test
    void createStrategy_ShouldReturnLoanStrategy_ForHipoteca() {
        ScoringGenerationRequest req = mock(ScoringGenerationRequest.class);
        when(req.getRequestType()).thenReturn("HIPOTECA");

        ScoringTransportStrategy strategy = ScoringTransportStrategyFactory.createStrategy(req);
        
        assertTrue(strategy instanceof LoanScoringTransportStrategy);
    }

    @Test
    void createStrategy_ShouldReturnCreditCardStrategy_ForTarjetaCredito() {
        ScoringGenerationRequest req = mock(ScoringGenerationRequest.class);
        when(req.getRequestType()).thenReturn("TARJETA_CREDITO");

        ScoringTransportStrategy strategy = ScoringTransportStrategyFactory.createStrategy(req);
        
        assertTrue(strategy instanceof CreditCardScoringTransportStrategy);
    }

    @Test
    void createStrategy_ShouldThrowException_WhenTypeIsInvalid() {
        ScoringGenerationRequest req = mock(ScoringGenerationRequest.class);
        when(req.getRequestType()).thenReturn("INVALID");

        assertThrows(IllegalArgumentException.class, () -> 
            ScoringTransportStrategyFactory.createStrategy(req)
        );
    }

    @Test
    void createStrategy_ShouldThrowException_WhenRequestOrTypeIsNull() {
        assertThrows(NullPointerException.class, () -> 
            ScoringTransportStrategyFactory.createStrategy(null)
        );

        ScoringGenerationRequest req = mock(ScoringGenerationRequest.class);
        when(req.getRequestType()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> 
            ScoringTransportStrategyFactory.createStrategy(req)
        );
    }
}
