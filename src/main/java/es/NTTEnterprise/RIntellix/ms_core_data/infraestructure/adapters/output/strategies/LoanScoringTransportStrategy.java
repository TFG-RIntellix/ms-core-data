package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output.strategies;


import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringTransportStrategy;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers.ScoringGenerationTransportDTOMapper;

/**
 * Strategy implementation for Loan and Mortgage scoring generation transport.
 *
 * Handles the transformation and message building for all fields required by
 * the scoring engine for loan and mortgage requests. Both PRESTAMO (Loan) and
 * HIPOTECA (Mortgage) use the same comprehensive feature set.
 *
 * Features included:
 * - Complete socio-demographic profile
 * - Detailed employment and financial information
 * - Full loan/mortgage parameters (term, interest rate, LTV, DTI)
 * - Credit history metrics
 *
 * @author Lucia Fernandez Mancebo
 * @Date 04-05-2026
 */
public class LoanScoringTransportStrategy implements ScoringTransportStrategy {


        public Object buildScoreGenerationPayload(ScoringGenerationRequest scoringGenerationRequest) {
                // Map domain entity to transport DTO with all loan-specific fields
                ScoringGenerationDTO scoringGenerationDTO = ScoringGenerationTransportDTOMapper
                                .toTransportDTO(scoringGenerationRequest);

                return scoringGenerationDTO;
        }

}
