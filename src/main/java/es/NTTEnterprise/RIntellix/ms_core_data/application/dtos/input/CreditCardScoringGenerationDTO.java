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
    private Double employmentSeniorityYears;
    private Integer dependents;
    // Financial features
    private Double annualIncome;
    private String incomeType;
    private String homeOwnership;
    private Double existingObligations;

    // Credit card specific features
    private String requestType;
    private Double creditLimit;
    private Boolean isRevolving;
    private Double interestRate;
    private Double lti;
    private Double dti;
    private Integer previousDefaultsCount;

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

    public Double getEmploymentSeniorityYears() {
        return employmentSeniorityYears;
    }

    public void setEmploymentSeniorityYears(Double employmentSeniorityYears) {
        this.employmentSeniorityYears = employmentSeniorityYears;
    }

    public Integer getDependents() {
        return dependents;
    }

    public void setDependents(Integer dependents) {
        this.dependents = dependents;
    }

    public Double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public String getHomeOwnership() {
        return homeOwnership;
    }

    public void setHomeOwnership(String homeOwnership) {
        this.homeOwnership = homeOwnership;
    }

    public Double getExistingObligations() {
        return existingObligations;
    }

    public void setExistingObligations(Double existingObligations) {
        this.existingObligations = existingObligations;
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

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getLti() {
        return lti;
    }

    public void setLti(Double lti) {
        this.lti = lti;
    }

    public Double getDti() {
        return dti;
    }

    public void setDti(Double dti) {
        this.dti = dti;
    }

    public Integer getPreviousDefaultsCount() {
        return previousDefaultsCount;
    }

    public void setPreviousDefaultsCount(Integer previousDefaultsCount) {
        this.previousDefaultsCount = previousDefaultsCount;
    }

    @Override
    public String toString() {
        return "CreditCardScoringGenerationDTO [requestId=" + requestId + ", partyId=" + partyId + ", age=" + age
                + ", gender=" + gender + ", maritalStatus=" + maritalStatus + ", employmentStatus="
                + employmentStatus + ", annualIncome=" + annualIncome + ", existingObligations=" + existingObligations
                + ", requestType=" + requestType + ", creditLimit=" + creditLimit + ", isRevolving=" + isRevolving + "]";
    }

}
