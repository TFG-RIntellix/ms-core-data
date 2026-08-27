package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Education;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.EmploymentStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Gender;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.HomeOwnership;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.MaritalStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.PartyType;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.PartyEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.ContactInfoEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.CreditHistoryEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.DemographicsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.EconomicDataEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.EmploymentEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.projections.PartyNameProjection;

class PartyMapperTest {

    private final PartyMapper mapper = new PartyMapper();

    @Test
    @DisplayName("Should map null to null")
    void toDomain_null() {
        assertNull(mapper.toDomain(null));
        assertNull(mapper.toPartialDomain(null));
    }

    @Test
    @DisplayName("Should map PartyEntity to Party domain successfully")
    void toDomain_success() {
        PartyEntity entity = new PartyEntity();
        entity.setId(new ObjectId());
        entity.setPartyType("INDIVIDUAL");

        DemographicsEntity demo = new DemographicsEntity();
        demo.setFirstName("John");
        demo.setLastName("Doe");
        demo.setNif("12345678Z");
        demo.setGender("HOMBRE");
        demo.setMaritalStatus("SOLTERO");
        demo.setEducation("GRADO");
        demo.setHomeOwnership("PROPIA_PAGADA");
        demo.setDependents(0);
        demo.setCountryOfResidence("ES");
        demo.setBirthDate(LocalDate.now());
        entity.setDemographics(demo);

        ContactInfoEntity contact = new ContactInfoEntity();
        contact.setEmail("j@d.com");
        contact.setPhoneNumber("123");
        contact.setAddress("Street");
        entity.setContactInfo(contact);

        EconomicDataEntity eco = new EconomicDataEntity();
        eco.setAnnualIncome(50000.0);
        eco.setCurrency("EUR");
        eco.setExistingObligations(100.0);
        eco.setHasMortgage(true);
        entity.setEconomicData(eco);

        EmploymentEntity emp = new EmploymentEntity();
        emp.setStatus("INDEFINIDO");
        emp.setOccupationSector("IT");
        emp.setEmployerSeniorityYears(5);
        entity.setEmployment(emp);

        CreditHistoryEntity ch = new CreditHistoryEntity();
        ch.setPreviousDefaultsCount(0);
        ch.setPreviousLoansCount(2);
        ch.setIsNewCustomer(false);
        ch.setPreviousLoansAmount(10000.0);
        entity.setCreditHistory(ch);

        Party party = mapper.toDomain(entity);

        assertEquals(entity.getId().toHexString(), party.getId());
        assertEquals(PartyType.INDIVIDUAL, party.getPartyType());

        var pd = party.getPersonDetails();
        assertEquals("John", pd.getFirstName());
        assertEquals("Doe", pd.getLastName());
        assertEquals("12345678Z", pd.getNif());

        var d = pd.getDemographics();
        assertEquals(Gender.HOMBRE, d.getGender());
        assertEquals(MaritalStatus.SOLTERO, d.getMaritalStatus());
        assertEquals(Education.GRADO, d.getEducation()); // Testing space replacement
        assertEquals(HomeOwnership.PROPIA_PAGADA, d.getHomeOwnership());
        assertEquals(0, d.getNrDependants());

        var ci = pd.getContactInfo();
        assertEquals("j@d.com", ci.getEmail());

        var f = pd.getFinancials();
        assertEquals(50000.0, f.getAnnualIncome().getAmount());
        assertEquals(100.0, f.getExistingObligations());
        assertTrue(f.getHasMortgage());
        assertEquals(EmploymentStatus.INDEFINIDO, f.getEmploymentStatus());
        assertEquals(5.0, f.getSeniorityYears());
        assertEquals(2, f.getPreviousLoansCount());
        assertEquals(10000.0, f.getPreviousLoansAmount().getAmount());
    }

    @Test
    @DisplayName("Should map PartyNameProjection to partial Party domain")
    void toPartialDomain_success() {
        PartyNameProjection proj = new PartyNameProjection() {
            @Override
            public String getId() {
                return "P-1";
            }

            @Override
            public DemographicsNameProjection getDemographics() {
                return new DemographicsNameProjection() {
                    @Override
                    public String getFirstName() {
                        return "Alice";
                    }

                    @Override
                    public String getLastName() {
                        return "Smith";
                    }
                };
            }
        };

        Party party = mapper.toPartialDomain(proj);

        assertEquals("P-1", party.getId());
        assertEquals("Alice", party.getPersonDetails().getFirstName());
        assertEquals("Smith", party.getPersonDetails().getLastName());
        assertNull(party.getPersonDetails().getNif());
    }
}
