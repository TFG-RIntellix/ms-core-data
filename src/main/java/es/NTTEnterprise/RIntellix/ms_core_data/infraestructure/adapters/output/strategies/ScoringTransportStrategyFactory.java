package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output.strategies;

import java.util.Objects;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringTransportStrategy;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;

/**
 * Factory for creating appropriate scoring transport strategies based on
 * request type.
 *
 * Implements the Factory pattern to encapsulate the logic of selecting
 * the correct strategy for different request types (Loans, Mortgages, Credit
 * Cards).
 *
 * Strategy selection logic:
 * - PRESTAMO (Loan): LoanScoringTransportStrategy
 * - HIPOTECA (Mortgage): LoanScoringTransportStrategy (same as loan)
 * - TARJETA_CREDITO (Credit Card): CreditCardScoringTransportStrategy
 *
 * @author Lucia Fernandez Mancebo
 * @Date 04-05-2026
 */
public abstract class ScoringTransportStrategyFactory {

    private ScoringTransportStrategyFactory() {
        throw new UnsupportedOperationException(LogMessage.EXCEPTION_MAPPER_NEVER_INSTANTIATE);
    }

    /**
     * Creates the appropriate scoring transport strategy based on the request type
     * from ScoringGenerationRequest.
     *
     * @param scoringGenerationRequest the scoring request with all features
     *                                 including
     *                                 request type; must not be null
     * @return a ScoringTransportStrategy suitable for the request type
     * @throws IllegalArgumentException if request type is not recognized
     * @throws NullPointerException     if scoring request or request type is null
     */
    public static ScoringTransportStrategy createStrategy(ScoringGenerationRequest scoringGenerationRequest) {
        Objects.requireNonNull(scoringGenerationRequest, "ScoringGenerationRequest cannot be null");
        Objects.requireNonNull(scoringGenerationRequest.getRequestType(), "Request type cannot be null");

        // Parse the request type from requestType string
        RequestType requestType = RequestType.fromValue(scoringGenerationRequest.getRequestType());

        return switch (requestType) {
            case PRESTAMO, HIPOTECA -> new LoanScoringTransportStrategy();
            case TARJETA_CREDITO -> new CreditCardScoringTransportStrategy();
            default -> throw new IllegalArgumentException(
                    "Unsupported request type: " + requestType);
        };
    }

}
