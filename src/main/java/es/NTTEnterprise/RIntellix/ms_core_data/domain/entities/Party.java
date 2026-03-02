package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.PartyType;

/**
 * Abstract base class representing a Party (customer).
 * A party can be an individual (Person) or a company.
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public class Party {

    private String id;
    private PartyType partyType;
    private Person personDetails;

    public Party() {
    }

    public Party(String id, PartyType partyType, Person personDetails) {
        this.id = id;
        this.partyType = partyType;
        this.personDetails = personDetails;
    }

    /**
     * Calculates the total debt of the party.
     * Must be implemented by subclasses.
     * @return the total debt as Money
     */
    public Money getTotalDebt() {
        return new Money(0.0, "EUR");
    }

    /**
     * Calculates the global Debt-To-Income ratio.
     * Must be implemented by subclasses.
     * @return the DTI ratio as a Double
     */
    public Double getGlobalDTI() {
        return 0.0;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PartyType getPartyType() {
        return partyType;
    }

    public void setPartyType(PartyType partyType) {
        this.partyType = partyType;
    }

    public Person getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(Person personDetails) {
        this.personDetails = personDetails;
    }
}
