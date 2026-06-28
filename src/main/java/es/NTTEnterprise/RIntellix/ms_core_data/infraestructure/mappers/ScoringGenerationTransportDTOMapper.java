package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;

/**
 * Infrastructure mapper for transforming domain scoring generation request
 * to transport DTO for Kafka serialization.
 *
 * This mapper bridges domain entities and infrastructure transport concerns,
 * ensuring the adapter layer remains clean of mapping logic. Follows the
 * static mapper pattern with null-safe field mapping.
 *
 * @author Lucia Fernandez Mancebo
 * @Date 03-22-2026
 */
public class ScoringGenerationTransportDTOMapper {

    private ScoringGenerationTransportDTOMapper() {
        throw new UnsupportedOperationException(LogMessage.EXCEPTION_MAPPER_NEVER_INSTANTIATE);
    }

    /**
     * Transforms domain ScoringGenerationRequest to transport DTO
     * for Kafka message serialization.
     *
     * Maps all 21 scoring feature fields from domain object to DTO payload.
     * Performs null-safe field access and assignment.
     *
     * @param scoringGenerationRequest the domain scoring generation request; must
     *                                 not be null
     * @return ScoringGenerationDTO containing all mapped fields; never null
     */
    public static ScoringGenerationDTO toTransportDTO(ScoringGenerationRequest scoringGenerationRequest) {
        if (scoringGenerationRequest == null) {
            return null;
        }

        ScoringGenerationDTO scoringGenerationDTO = new ScoringGenerationDTO();

        scoringGenerationDTO.setRequestId(scoringGenerationRequest.getRequestId());
        scoringGenerationDTO.setPartyId(scoringGenerationRequest.getPartyId());
        scoringGenerationDTO.setAge(scoringGenerationRequest.getAge());
        scoringGenerationDTO.setGender(scoringGenerationRequest.getGender());
        scoringGenerationDTO.setMaritalStatus(scoringGenerationRequest.getMaritalStatus());
        scoringGenerationDTO.setEducation(scoringGenerationRequest.getEducation());
        scoringGenerationDTO.setDependents(scoringGenerationRequest.getDependents());
        scoringGenerationDTO.setHomeOwnership(scoringGenerationRequest.getHomeOwnership());
        scoringGenerationDTO.setHasMortgage(scoringGenerationRequest.getHasMortgage());
        scoringGenerationDTO.setEmploymentStatus(scoringGenerationRequest.getEmploymentStatus());
        scoringGenerationDTO.setOccupationSector(scoringGenerationRequest.getOccupationSector());
        scoringGenerationDTO.setAnnualIncome(scoringGenerationRequest.getAnnualIncome());
        scoringGenerationDTO.setExistingObligations(scoringGenerationRequest.getExistingObligations());
        scoringGenerationDTO.setRequestType(scoringGenerationRequest.getRequestType());
        scoringGenerationDTO.setPurpose(scoringGenerationRequest.getPurpose());
        scoringGenerationDTO.setLoanType(scoringGenerationRequest.getLoanType());
        scoringGenerationDTO.setLoanAmount(scoringGenerationRequest.getLoanAmount());
        scoringGenerationDTO.setTermMonths(scoringGenerationRequest.getTermMonths());
        scoringGenerationDTO.setInterestRate(scoringGenerationRequest.getInterestRate());
        scoringGenerationDTO.setLtv(scoringGenerationRequest.getLtv());
        scoringGenerationDTO.setDti(scoringGenerationRequest.getDti());
        scoringGenerationDTO.setPreviousLoansCount(scoringGenerationRequest.getPreviousLoansCount());
        scoringGenerationDTO.setPreviousDefaultsCount(scoringGenerationRequest.getPreviousDefaultsCount());

        return scoringGenerationDTO;
    }
}
