package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

import java.time.LocalDate;

/**
 * Embedded document for party demographics information.
 * Contains personal identification and demographic data.
 * 
 * @author Lucía Fernández Mancebo
 * @date 01/03/2026
 */
public class DemographicsEntity {
    private String nif;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private String maritalStatus;
    private String countryOfResidence;
    private String education;
    private String homeOwnership;
    private Integer dependents;

    public DemographicsEntity() {
    }

    // Getters and Setters

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getHomeOwnership() {
        return homeOwnership;
    }

    public void setHomeOwnership(String homeOwnership) {
        this.homeOwnership = homeOwnership;
    }

    public Integer getDependents() {
        return dependents;
    }

    public void setDependents(Integer dependents) {
        this.dependents = dependents;
    }
}
