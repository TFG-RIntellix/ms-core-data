package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Contract;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialProfile;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Money;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.MortgageContract;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Person;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.SocioDemographicProfile;

/**
 * Mapper class to convert Request + Party entities into a domain scoring
 * payload.
 *
 * Extracts and transforms domain entities into a ScoringGenerationRequest with
 * all
 * features required by the scoring model engine.
 *
 * Mapping assumptions:
 * - Request and Party entities are always present (not null)
 * - Person details structure is complete when present
 * - Only optional Money and numeric values are checked for null
 *
 * @author Lucia Fernandez Mancebo
 * @Date 03-22-2026
 */
@Component
public class ScoringGenerationDTOMapper {

    /**
     * Maps a Request and Party to a ScoringGenerationRequest with all scoring
     * features.
     *
     * @param request the request entity with loan details
     * @param party   the party entity with customer information
     * @return the scoring generation domain payload with extracted features
     */
    public ScoringGenerationRequest toDomain(Request request, Party party) {
        ScoringGenerationRequest scoringGenerationRequest = new ScoringGenerationRequest();

        // Identifiers
        scoringGenerationRequest.setRequestId(request.getId());
        scoringGenerationRequest.setPartyId(party.getId());
        Person person = party.getPersonDetails();

        // Extract socio-demographic features
        mapSocioDemographicFeatures(person, scoringGenerationRequest);

        // Extract financial features
        mapFinancialFeatures(person, scoringGenerationRequest);

        // Extract request features
        mapRequestFeatures(request, scoringGenerationRequest);

        return scoringGenerationRequest;
    }

    private void mapSocioDemographicFeatures(Person person, ScoringGenerationRequest scoringGenerationRequest) {
        SocioDemographicProfile demographics = person.getDemographics();

        scoringGenerationRequest.setAge(demographics.getAge());
        scoringGenerationRequest.setGender(demographics.getGender().toString());
        scoringGenerationRequest.setMaritalStatus(demographics.getMaritalStatus().toString());
        scoringGenerationRequest.setEducation(demographics.getEducation().toString());
        scoringGenerationRequest.setDependents(demographics.getNrDependants());
        scoringGenerationRequest.setHomeOwnership(demographics.getHomeOwnership().toString());
        scoringGenerationRequest.setHasMortgage(hasMortgageInContracts(person.getActiveContracts()));
    }

    private void mapFinancialFeatures(Person person, ScoringGenerationRequest scoringGenerationRequest) {
        FinancialProfile financials = person.getFinancials();

        scoringGenerationRequest.setEmploymentStatus(financials.getEmploymentStatus().toString());
        scoringGenerationRequest.setOccupationSector(financials.getOccupation());

        Money annualIncome = financials.getAnnualIncome();
        if (annualIncome != null) {
            scoringGenerationRequest.setAnnualIncome(annualIncome.getAmount());
        }

        scoringGenerationRequest.setPreviousLoansCount(financials.getPreviousLoansCount());
        scoringGenerationRequest.setPreviousDefaultsCount(financials.getPreviousDefaultsCount());

        // DTI calculated from all active contracts and income
        scoringGenerationRequest.setDti(person.getGlobalDTI());
    }

    private void mapRequestFeatures(Request request, ScoringGenerationRequest scoringGenerationRequest) {
        var details = request.getRequestDetails();

        scoringGenerationRequest.setLoanType(details.getRequestType().toString());
        scoringGenerationRequest.setPurpose(details.getPurpose().toString());

        Money requestedAmount = details.getRequestedAmount();
        if (requestedAmount != null) {
            scoringGenerationRequest.setLoanAmount(requestedAmount.getAmount());
        }

        scoringGenerationRequest.setTermMonths(details.getTermMonths());
        scoringGenerationRequest.setInterestRate(details.getInterestRate());

        // LTV calculated by domain logic (applicable for mortgages)
        Double ltv = request.calculateLTV();
        if (ltv != null) {
            scoringGenerationRequest.setLtv(ltv);
        }
    }

    private boolean hasMortgageInContracts(List<Contract> contracts) {
        if (contracts == null || contracts.isEmpty()) {
            return false;
        }
        return contracts.stream().anyMatch(contract -> contract instanceof MortgageContract);
    }
}
