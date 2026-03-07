package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.util.Objects;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.PartyType;

/**
 * Abstract base class representing a Party (customer).
 * A party can be an individual (Person) or a company.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public class Party {

    private String id;
    private PartyType partyType;
    private Person personDetails;

    /**
     * Constructor of the Party class.
     */
    public Party() {
    }

    /**
     * Constructor of the Party class.
     * 
     * @param id            the unique identifier of the party
     * @param partyType     the type of the party (individual or company)
     * @param personDetails the personal details of the party (applicable if the
     *                      party is an individual)
     */
    public Party(String id, PartyType partyType, Person personDetails) {
        this.id = id;
        this.partyType = partyType;
        this.personDetails = personDetails;
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

    // toString, hashCode and equals

    @Override
    public String toString() {
        return "Party [id=" + id + ", partyType=" + partyType + ", personDetails=" + personDetails + "]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.partyType);
        hash = 67 * hash + Objects.hashCode(this.personDetails);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Party other = (Party) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.partyType != other.partyType) {
            return false;
        }
        return Objects.equals(this.personDetails, other.personDetails);
    }
}
