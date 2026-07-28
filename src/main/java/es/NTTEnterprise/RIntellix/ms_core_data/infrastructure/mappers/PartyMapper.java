package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers;

import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ContactInfo;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialProfile;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Money;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Person;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.SocioDemographicProfile;
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

/**
 * Mapper class to convert between PartyEntity (infrastructure) and Party/Person
 * (domain).
 * 
 * @author Lucía Fernández Mancebo
 * @date 01/03/2026
 */
@Component
public class PartyMapper {

    /**
     * Converts a PartyEntity from infrastructure layer to a Party domain object.
     * 
     * @param entity the PartyEntity to convert
     * @return the Party domain object, or null if entity is null
     */
    public Party toDomain(PartyEntity entity) {
        if (entity == null) {
            return null;
        }

        Party party = new Party();
        party.setId(entity.getId().toHexString());
        party.setPartyType(mapPartyType(entity.getPartyType()));

        // Map Person details
        Person person = mapPerson(entity);
        party.setPersonDetails(person);

        return party;
    }

    /**
     * Maps PartyEntity data to a Person domain object.
     */
    private Person mapPerson(PartyEntity entity) {
        Person person = new Person();

        // Map identity from demographics
        DemographicsEntity demographics = entity.getDemographics();
        person.setFirstName(demographics.getFirstName());
        person.setLastName(demographics.getLastName());
        person.setNif(demographics.getNif());

        // Map SocioDemographicProfile
        person.setDemographics(mapSocioDemographicProfile(demographics));

        // Map ContactInfo
        person.setContactInfo(mapContactInfo(entity.getContactInfo()));

        // Map FinancialProfile
        person.setFinancials(mapFinancialProfile(entity));

        return person;
    }

    /**
     * Maps DemographicsEntity to SocioDemographicProfile domain object.
     */
    private SocioDemographicProfile mapSocioDemographicProfile(DemographicsEntity entity) {
        SocioDemographicProfile profile = new SocioDemographicProfile();
        profile.setBirthDate(entity.getBirthDate());
        profile.setGender(mapGender(entity.getGender()));
        profile.setMaritalStatus(mapMaritalStatus(entity.getMaritalStatus()));
        profile.setEducation(mapEducation(entity.getEducation()));
        profile.setHomeOwnership(mapHomeOwnership(entity.getHomeOwnership()));
        profile.setCountryOfResidence(entity.getCountryOfResidence());
        profile.setNrDependants(entity.getNrDependants());

        return profile;
    }

    /**
     * Maps ContactInfoEntity to ContactInfo domain object.
     */
    private ContactInfo mapContactInfo(ContactInfoEntity entity) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setPhoneNumber(entity.getPhoneNumber());
        contactInfo.setEmail(entity.getEmail());
        contactInfo.setAddress(entity.getAddress());

        return contactInfo;
    }

    /**
     * Maps EconomicDataEntity, EmploymentEntity and CreditHistoryEntity to
     * FinancialProfile domain object.
     */
    private FinancialProfile mapFinancialProfile(PartyEntity entity) {
        FinancialProfile profile = new FinancialProfile();

        // Map from EconomicDataEntity
        EconomicDataEntity economicData = entity.getEconomicData();
        Money annualIncome = new Money(economicData.getAnnualIncome(), economicData.getCurrency());
        profile.setAnnualIncome(annualIncome);
        profile.setExistingObligations(
            economicData.getExistingObligations() != null ? economicData.getExistingObligations() : 0.0);
        profile.setHasMortage(
            economicData.getHasMortage() != null ? economicData.getHasMortage() : false);

        // Map from EmploymentEntity
        EmploymentEntity employment = entity.getEmployment();
        profile.setEmploymentStatus(mapEmploymentStatus(employment.getStatus()));
        profile.setOccupationSector(employment.getSector());
        if (employment.getEmployerSeniorityYears() != null) {
            profile.setSeniorityYears(employment.getEmployerSeniorityYears().doubleValue());
        }

        // Map from CreditHistoryEntity
        CreditHistoryEntity creditHistory = entity.getCreditHistory();
        if (creditHistory != null) {
            profile.setPreviousDefaultsCount(creditHistory.getPreviousDefaultsCount());
            profile.setPreviousLoansCount(creditHistory.getPreviousLoansCount());
            profile.setIsNewCustomer(creditHistory.getIsNewCustomer());

            String currency = economicData.getCurrency();
            if (creditHistory.getPreviousLoansAmount() != null) {
                profile.setPreviousLoansAmount(new Money(creditHistory.getPreviousLoansAmount(), currency));
            }
            if (creditHistory.getPreviousRepaymentsAmount() != null) {
                profile.setPreviousRepaymentsAmount(new Money(creditHistory.getPreviousRepaymentsAmount(), currency));
            }
        }

        return profile;
    }

    /**
     * Maps string value to PartyType enum.
     * Returns null if value is null or invalid enum value.
     */
    private PartyType mapPartyType(String value) {
        if (value == null) {
            return null;
        }
        try {
            return PartyType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Maps string value to Gender enum.
     * Returns null if value is null or invalid enum value.
     */
    private Gender mapGender(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Gender.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Maps string value to MaritalStatus enum.
     * Returns null if value is null or invalid enum value.
     */
    private MaritalStatus mapMaritalStatus(String value) {
        if (value == null) {
            return null;
        }
        try {
            return MaritalStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Maps string value to Education enum, replacing spaces with underscores.
     * Returns null if value is null or invalid enum value.
     */
    private Education mapEducation(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Education.valueOf(value.replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Maps string value to HomeOwnership enum.
     * Returns null if value is null or invalid enum value.
     */
    private HomeOwnership mapHomeOwnership(String value) {
        if (value == null) {
            return null;
        }
        try {
            return HomeOwnership.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Maps string value to EmploymentStatus enum.
     * Returns null if value is null or invalid enum value.
     */
    private EmploymentStatus mapEmploymentStatus(String value) {
        if (value == null) {
            return null;
        }
        try {
            return EmploymentStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Converts a PartyNameProjection to a partial Party domain object.
     * Only populates the Party with Person containing firstName and lastName.
     * Used for efficient queries when only the party name is needed.
     * 
     * @param projection the PartyNameProjection from the database query
     * @return a partial Party with only name fields populated, or null if
     *         projection is null
     */
    public Party toPartialDomain(PartyNameProjection projection) {
        if (projection == null) {
            return null;
        }

        Party party = new Party();
        party.setId(projection.getId());

        Person person = new Person();
        if (projection.getDemographics() != null) {
            person.setFirstName(projection.getDemographics().getFirstName());
            person.setLastName(projection.getDemographics().getLastName());
        }
        party.setPersonDetails(person);

        return party;
    }
}
