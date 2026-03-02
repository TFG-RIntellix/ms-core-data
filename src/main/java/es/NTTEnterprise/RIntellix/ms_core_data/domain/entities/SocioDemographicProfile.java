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
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public class SocioDemographicProfile {

    private LocalDate birthDate;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Education education;
    private HomeOwnership homeOwnership;
    private String countryOfResidence;
    private Integer nrDependants;

    public SocioDemographicProfile() {
    }

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
}
