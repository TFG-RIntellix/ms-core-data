package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.ContactInfoEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.CreditHistoryEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.DemographicsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.EconomicDataEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.EmploymentEntity;

/**
 * Entity representing a Party (customer) in MongoDB.
 * A party can be an individual or a company.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
@Document(collection = "parties")
public class PartyEntity {

    @Id
    private String id;

    @Field("party_type")
    private String partyType;

    @Field("demographics")
    private DemographicsEntity demographics;

    @Field("contact_info")
    private ContactInfoEntity contactInfo;

    @Field("employment")
    private EmploymentEntity employment;

    @Field("economic_data")
    private EconomicDataEntity economicData;

    @Field("credit_history")
    private CreditHistoryEntity creditHistory;

    public PartyEntity() {
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public DemographicsEntity getDemographics() {
        return demographics;
    }

    public void setDemographics(DemographicsEntity demographics) {
        this.demographics = demographics;
    }

    public ContactInfoEntity getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfoEntity contactInfo) {
        this.contactInfo = contactInfo;
    }

    public EmploymentEntity getEmployment() {
        return employment;
    }

    public void setEmployment(EmploymentEntity employment) {
        this.employment = employment;
    }

    public EconomicDataEntity getEconomicData() {
        return economicData;
    }

    public void setEconomicData(EconomicDataEntity economicData) {
        this.economicData = economicData;
    }

    public CreditHistoryEntity getCreditHistory() {
        return creditHistory;
    }

    public void setCreditHistory(CreditHistoryEntity creditHistory) {
        this.creditHistory = creditHistory;
    }
}
