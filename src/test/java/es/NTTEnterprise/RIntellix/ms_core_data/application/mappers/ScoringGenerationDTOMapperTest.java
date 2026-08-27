package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialProfile;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Money;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Person;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RequestDetails;
import java.time.LocalDate;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.SocioDemographicProfile;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Education;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.EmploymentStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Gender;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.HomeOwnership;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.MaritalStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;

class ScoringGenerationDTOMapperTest {

    private final ScoringGenerationDTOMapper mapper = new ScoringGenerationDTOMapper();

    @Test
    @DisplayName("Should map Request and Party to ScoringGenerationRequest successfully")
    void toOutputDTO_success() {
        Request request = new Request();
        request.setId("REQ-1");
        RequestDetails details = new RequestDetails(RequestType.PRESTAMO, Purpose.REFORMA_HOGAR, new Money(20000.0, "EUR"), 36, 6.0, null, false, "FIXED", "Personal");
        request.setRequestDetails(details);

        Party party = new Party();
        party.setId("P-1");
        Person pd = new Person();

        SocioDemographicProfile demo = new SocioDemographicProfile();
        demo.setBirthDate(LocalDate.now().minusYears(40));
        demo.setGender(Gender.HOMBRE);
        demo.setMaritalStatus(MaritalStatus.CASADO);
        demo.setEducation(Education.GRADO);
        demo.setNrDependants(2);
        demo.setHomeOwnership(HomeOwnership.PROPIA_PAGADA);
        pd.setDemographics(demo);

        FinancialProfile fin = new FinancialProfile();
        fin.setEmploymentStatus(EmploymentStatus.INDEFINIDO);
        fin.setOccupationSector("IT");
        fin.setSeniorityYears(10.0);
        fin.setAnnualIncome(new Money(60000.0, "EUR"));
        fin.setPreviousLoansCount(1);
        fin.setPreviousDefaultsCount(0);
        fin.setExistingObligations(500.0);
        fin.setHasMortgage(true);
        pd.setFinancials(fin);
        
        party.setPersonDetails(pd);

        ScoringGenerationRequest dto = mapper.toOutputDTO(request, party);

        assertEquals("REQ-1", dto.getRequestId());
        assertEquals("P-1", dto.getPartyId());
        
        // Demographics
        assertEquals(40, dto.getAge());
        assertEquals("HOMBRE", dto.getGender());
        assertEquals("CASADO", dto.getMaritalStatus());
        assertEquals("GRADO", dto.getEducation());
        assertEquals(2, dto.getDependents());
        assertEquals("PROPIA_PAGADA", dto.getHomeOwnership());
        assertTrue(dto.getHasMortgage());

        // Financials
        assertEquals("INDEFINIDO", dto.getEmploymentStatus());
        assertEquals("IT", dto.getOccupationSector());
        assertEquals(10.0, dto.getEmploymentSeniorityYears());
        assertEquals("Salario", dto.getIncomeType()); // Verify mapping
        assertEquals(60000.0, dto.getAnnualIncome());
        assertEquals(1, dto.getPreviousLoansCount());
        assertEquals(0, dto.getPreviousDefaultsCount());
        assertEquals(500.0, dto.getExistingObligations());
        assertEquals(0.008333, dto.getDti(), 0.001);

        // Request
        assertEquals("PRESTAMO", dto.getRequestType());
        assertEquals("REFORMA_HOGAR", dto.getPurpose());
        assertEquals("Personal", dto.getLoanType());
        assertEquals(20000.0, dto.getLoanAmount());
        assertEquals(36, dto.getTermMonths());
        assertEquals(6.0, dto.getInterestRate());
        assertFalse(dto.getIsRevolving());
    }

    @Test
    @DisplayName("Should correctly map income types")
    void calculateIncomeType_mappings() {
        assertEquals("Salario", mapper.calculateIncomeType("INDEFINIDO"));
        assertEquals("Salario", mapper.calculateIncomeType("TEMPORAL"));
        assertEquals("Salario", mapper.calculateIncomeType("FUNCIONARIO"));
        assertEquals("Autonomo", mapper.calculateIncomeType("AUTONOMO"));
        assertEquals("Ayudas", mapper.calculateIncomeType("DESEMPLEADO"));
        assertEquals("Pension", mapper.calculateIncomeType("INACTIVO"));
        assertEquals("Otro", mapper.calculateIncomeType("UNKNOWN")); // Default
    }
}
