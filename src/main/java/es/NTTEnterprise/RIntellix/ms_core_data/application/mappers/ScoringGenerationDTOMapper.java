package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Contract;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialProfile;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Money;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.MortgageContract;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Person;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.SocioDemographicProfile;

/**
 * Mapper class to convert between Request + Party (domain) and
 * ScoringGenerationDTO (application).
 * Extracts and transforms domain entities into a DTO containing all features
 * required by the
 * scoring model engine.
 * 
 * Mapping assumptions:
 * - Request and Party entities are always present (not null)
 * - Person details structure is complete when present
 * - Only optional Money and numeric values are checked for null
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-15-2026
 */
@Component
public class ScoringGenerationDTOMapper {

    /**
     * Maps a Request and Party to a ScoringGenerationDTO with all scoring features.
     * 
     * @param request the request entity with loan details
     * @param party   the party entity with customer information
     * @return the scoring generation DTO with extracted features
     */
    public ScoringGenerationDTO toDTO(Request request, Party party) {
        ScoringGenerationDTO dto = new ScoringGenerationDTO();

        // Identifiers
        dto.setRequestId(request.getId());
        dto.setPartyId(party.getId());
        Person person = party.getPersonDetails();

        // Extract socio-demographic features
        mapSocioDemographicFeatures(person, dto);

        // Extract financial features
        mapFinancialFeatures(person, dto);

        // Extract request features
        mapRequestFeatures(request, dto);

        return dto;
    }

    private void mapSocioDemographicFeatures(Person person, ScoringGenerationDTO dto) {
        SocioDemographicProfile demographics = person.getDemographics();

        dto.setAge(demographics.getAge());
        dto.setGender(demographics.getGender().toString());
        dto.setMaritalStatus(demographics.getMaritalStatus().toString());
        dto.setEducation(demographics.getEducation().toString());
        dto.setDependents(demographics.getNrDependants());
        dto.setHomeOwnership(demographics.getHomeOwnership().toString());
        dto.setHasMortgage(hasMortgageInContracts(person.getActiveContracts()));
    }

    private void mapFinancialFeatures(Person person, ScoringGenerationDTO dto) {
        FinancialProfile financials = person.getFinancials();

        dto.setEmploymentStatus(financials.getEmploymentStatus().toString());
        dto.setOccupationSector(financials.getOccupation());

        Money annualIncome = financials.getAnnualIncome();
        if (annualIncome != null) {
            dto.setAnnualIncome(annualIncome.getAmount());
        }

        dto.setPreviousLoansCount(financials.getPreviousLoansCount());
        dto.setPreviousDefaultsCount(financials.getPreviousDefaultsCount());

        // DTI calculated from all active contracts and income
        dto.setDti(person.getGlobalDTI());
    }

    private void mapRequestFeatures(Request request, ScoringGenerationDTO dto) {
        var details = request.getRequestDetails();

        dto.setLoanType(details.getRequestType().toString());
        dto.setPurpose(details.getPurpose().toString());

        Money requestedAmount = details.getRequestedAmount();
        if (requestedAmount != null) {
            dto.setLoanAmount(requestedAmount.getAmount());
        }

        dto.setTermMonths(details.getTermMonths());
        dto.setInterestRate(details.getInterestRate());

        // LTV calculated by domain logic (applicable for mortgages)
        Double ltv = request.calculateLTV();
        if (ltv != null) {
            dto.setLtv(ltv);
        }
    }

    private boolean hasMortgageInContracts(List<Contract> contracts) {
        if (contracts == null || contracts.isEmpty()) {
            return false;
        }
        return contracts.stream().anyMatch(contract -> contract instanceof MortgageContract);
    }
}
