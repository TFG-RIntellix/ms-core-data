package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.time.LocalDate;
import java.time.Period;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Education;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Gender;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.HomeOwnership;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.MaritalStatus;

/**
 * Value object representing the socio-demographic profile of a person.
 * Contains demographic and social information.
 * 
 * @author Lucía Fernández Mancebo
 * @date 01/03/2026
 */
public class SocioDemographicProfile {

    private LocalDate birthDate;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Education education;
    private HomeOwnership homeOwnership;
    private String countryOfResidence;
    private Integer nrDependants;

    /**
     * Constructor of the SocioDemographicProfile class.
     */
    public SocioDemographicProfile() {
    }

    /**
     * Constructor of the SocioDemographicProfile class.
     * 
     * @param birthDate          the birth date of the person
     * @param gender             the gender of the person
     * @param maritalStatus      the marital status of the person
     * @param education          the education level of the person
     * @param homeOwnership      the home ownership status of the person
     * @param countryOfResidence the country of residence of the person
     * @param nrDependants       the number of dependants of the person
     */
    public SocioDemographicProfile(LocalDate birthDate, Gender gender, MaritalStatus maritalStatus,
            Education education, HomeOwnership homeOwnership,
            String countryOfResidence, Integer nrDependants) {
        this.birthDate = birthDate;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.education = education;
        this.homeOwnership = homeOwnership;
        this.countryOfResidence = countryOfResidence;
        this.nrDependants = nrDependants;
    }

    /**
     * Calculates the age of the person based on the birth date.
     * 
     * @return the age in years
     */
    public Integer getAge() {
        if (birthDate == null) {
            return null;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // Getters and Setters

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public HomeOwnership getHomeOwnership() {
        return homeOwnership;
    }

    public void setHomeOwnership(HomeOwnership homeOwnership) {
        this.homeOwnership = homeOwnership;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public Integer getNrDependants() {
        return nrDependants;
    }

    public void setNrDependants(Integer nrDependants) {
        this.nrDependants = nrDependants;
    }

    // toString, hashCode and equals
    @Override
    public String toString() {
        return "SocioDemographicProfile [birthDate=" + birthDate + ", gender=" + gender + ", maritalStatus="
                + maritalStatus + ", education=" + education + ", homeOwnership=" + homeOwnership
                + ", countryOfResidence=" + countryOfResidence + ", nrDependants=" + nrDependants + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((maritalStatus == null) ? 0 : maritalStatus.hashCode());
        result = prime * result + ((education == null) ? 0 : education.hashCode());
        result = prime * result + ((homeOwnership == null) ? 0 : homeOwnership.hashCode());
        result = prime * result + ((countryOfResidence == null) ? 0 : countryOfResidence.hashCode());
        result = prime * result + ((nrDependants == null) ? 0 : nrDependants.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SocioDemographicProfile other = (SocioDemographicProfile) obj;
        if (birthDate == null) {
            if (other.birthDate != null)
                return false;
        } else if (!birthDate.equals(other.birthDate))
            return false;
        if (gender != other.gender)
            return false;
        if (maritalStatus != other.maritalStatus)
            return false;
        if (education != other.education)
            return false;
        if (homeOwnership != other.homeOwnership)
            return false;
        if (countryOfResidence == null) {
            if (other.countryOfResidence != null)
                return false;
        } else if (!countryOfResidence.equals(other.countryOfResidence))
            return false;
        if (nrDependants == null) {
            if (other.nrDependants != null)
                return false;
        } else if (!nrDependants.equals(other.nrDependants))
            return false;
        return true;
    }

}
