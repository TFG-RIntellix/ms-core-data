package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

/**
 * Data Transfer Object (DTO) for credit card scoring generation.
 * Contains a subset of features required by the scoring engine model,
 * tailored specifically for credit card requests.
 *
 * Unlike standard loan/mortgage requests, credit card scoring focuses on:
 * - Socio-demographic data (age, gender, marital status, employment, income)
 * - Credit card-specific parameters (credit limit, revolving status)
 * - Basic request details (type, purpose)
 *
 * This specialized DTO reduces the payload size and ensures only relevant
 * data is sent to the scoring engine for credit cards, following the
 * Strategy Pattern for type-specific message transport.
 *
 * @author Lucia Fernandez Mancebo
 * @Date 04-05-2026
 */
public class CreditCardScoringGenerationDTO {

    private String requestId;
    private String partyId;

    // Socio-demographic features
    private Integer age;
    private String gender;
    private String maritalStatus;
    private String employmentStatus;

    // Financial features
    private Double annualIncome;

    // Credit card specific features
    private String requestType;
    private Double creditLimit;
    private Boolean isRevolving;

    /**
     * Creates an empty credit card scoring generation DTO.
     */
    public CreditCardScoringGenerationDTO() {
    }

    // Getters and Setters

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Boolean getIsRevolving() {
        return isRevolving;
    }

    public void setIsRevolving(Boolean isRevolving) {
        this.isRevolving = isRevolving;
    }

    @Override
    public String toString() {
        return "CreditCardScoringGenerationDTO [requestId=" + requestId + ", partyId=" + partyId + ", age=" + age
                + ", gender=" + gender + ", maritalStatus=" + maritalStatus + ", employmentStatus="
                + employmentStatus + ", annualIncome=" + annualIncome + ", requestType=" + requestType
                + ", creditLimit=" + creditLimit + ", isRevolving=" + isRevolving + "]";
    }

}
