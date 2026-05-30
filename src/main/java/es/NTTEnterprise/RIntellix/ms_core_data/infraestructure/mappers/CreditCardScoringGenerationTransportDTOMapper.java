package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreditCardScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;

/**
 * Infrastructure mapper for transforming domain scoring generation request
 * to credit card transport DTO for Kafka serialization.
 *
 * This mapper bridges domain entities and infrastructure transport concerns
 * for credit card specific payloads, ensuring the adapter layer remains clean
 * of mapping logic. Follows the static mapper pattern with null-safe field
 * mapping.
 *
 * Extracts only the fields relevant to credit card scoring:
 * - Socio-demographic data (age, gender, marital status, employment, income)
 * - Credit card parameters (credit limit, revolving status)
 * - Request identification and type
 *
 * All required credit card fields are now present in ScoringGenerationRequest
 * after mapping from Request entity, making this mapper fully self-contained.
 *
 * @author Lucia Fernandez Mancebo
 * @Date 04-05-2026
 */
public class CreditCardScoringGenerationTransportDTOMapper {

    private CreditCardScoringGenerationTransportDTOMapper() {
        throw new UnsupportedOperationException("Never instantiate");
    }

    /**
     * Transforms domain ScoringGenerationRequest to credit card transport DTO
     * for Kafka message serialization.
     *
     * Maps only the 10 credit card specific fields from domain object to DTO
     * payload.
     * Filters out loan-specific fields (LTV, DTI, term months, purpose, etc).
     * Performs null-safe field access and assignment.
     *
     * All credit card-specific fields (creditLimit, isRevolving) are extracted
     * from ScoringGenerationRequest, which is populated by
     * ScoringGenerationDTOMapper
     * from the request's RequestDetails.
     *
     * @param scoringGenerationRequest the domain scoring generation request; must
     *                                 not be null
     * @return CreditCardScoringGenerationDTO containing mapped fields; never null
     */
    public static CreditCardScoringGenerationDTO toTransportDTO(ScoringGenerationRequest scoringGenerationRequest) {
        if (scoringGenerationRequest == null) {
            return null;
        }

        CreditCardScoringGenerationDTO dto = new CreditCardScoringGenerationDTO();

        dto.setRequestId(scoringGenerationRequest.getRequestId());
        dto.setPartyId(scoringGenerationRequest.getPartyId());
        dto.setAge(scoringGenerationRequest.getAge());
        dto.setGender(scoringGenerationRequest.getGender());
        dto.setMaritalStatus(scoringGenerationRequest.getMaritalStatus());
        dto.setEmploymentStatus(scoringGenerationRequest.getEmploymentStatus());
        dto.setAnnualIncome(scoringGenerationRequest.getAnnualIncome());
        dto.setRequestType(scoringGenerationRequest.getRequestType());
        dto.setCreditLimit(scoringGenerationRequest.getCreditLimit());
        dto.setIsRevolving(scoringGenerationRequest.getIsRevolving());

        return dto;
    }

}
