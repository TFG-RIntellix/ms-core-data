package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output.strategies;


import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreditCardScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringTransportStrategy;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers.CreditCardScoringGenerationTransportDTOMapper;

/**
 * Strategy implementation for Credit Card scoring generation transport.
 *
 * Handles the transformation and message building for credit card specific
 * scoring requests. Credit cards require a different, more focused feature set
 * compared to loans and mortgages.
 *
 * Features included:
 * - Core socio-demographic profile (age, gender, marital status)
 * - Employment and income information
 * - Credit card-specific parameters (credit limit, revolving status)
 *
 * All credit card fields are already present in ScoringGenerationRequest,
 * populated during the mapping phase from the Request entity's RequestDetails.
 * This strategy is stateless and self-contained.
 *
 * @author Lucía Fernández Mancebo
 * @date 05/04/2026
 */
public class CreditCardScoringTransportStrategy implements ScoringTransportStrategy {


        /**
         * Constructs the strategy (stateless).
         */
        public CreditCardScoringTransportStrategy() {
        }

        public Object buildScoreGenerationPayload(ScoringGenerationRequest scoringGenerationRequest) {
                // Map domain entity to credit card transport DTO with focused fields
                CreditCardScoringGenerationDTO creditCardDTO = CreditCardScoringGenerationTransportDTOMapper
                                .toTransportDTO(scoringGenerationRequest);

                return creditCardDTO;
        }

}
