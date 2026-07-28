package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document for party demographics information.
 * Contains personal identification and demographic data.
 * 
 * @author Lucía Fernández Mancebo
 * @date 01/03/2026
 */
public class DemographicsEntity {

    @Field("nif")
    private String nif;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("birth_date")
    private LocalDate birthDate;

    @Field("gender")
    private String gender;

    @Field("marital_status")
    private String maritalStatus;

    @Field("country_of_residence")
    private String countryOfResidence;

    @Field("education")
    private String education;

    @Field("home_ownership")
    private String homeOwnership;

    @Field("nr_dependants")
    private Integer nrDependants;

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

    public Integer getNrDependants() {
        return nrDependants;
    }

    public void setNrDependants(Integer nrDependants) {
        this.nrDependants = nrDependants;
    }
}
