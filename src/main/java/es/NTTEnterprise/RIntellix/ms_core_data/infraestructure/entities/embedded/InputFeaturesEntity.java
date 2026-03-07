package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded entity representing the input features snapshot stored in a scoring
 * document.
 * Maps the "input_features" sub-document from the "scorings" MongoDB
 * collection.
 * Contains all features used as input for the credit risk model.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public class InputFeaturesEntity {

    @Field("age")
    private Integer age;

    @Field("gender")
    private String gender;

    @Field("marital_status")
    private String maritalStatus;

    @Field("education")
    private String education;

    @Field("employment_status")
    private String employmentStatus;

    @Field("work_sector")
    private String workSector;

    @Field("nr_dependants")
    private Integer nrDependants;

    @Field("home_ownership")
    private String homeOwnership;

    @Field("has_mortgage")
    private Boolean hasMortgage;

    @Field("annual_income")
    private Double annualIncome;

    @Field("request_type")
    private String requestType;

    @Field("purpose")
    private String purpose;

    @Field("requested_amount")
    private Double requestedAmount;

    @Field("term_months")
    private Integer termMonths;

    @Field("interest_rate")
    private Double interestRate;

    @Field("ltv")
    private Double ltv;

    @Field("dti")
    private Double dti;

    @Field("previous_loans_count")
    private Integer previousLoansCount;

    @Field("previous_defaults_count")
    private Integer previousDefaultsCount;

    public InputFeaturesEntity() {
    }

    // Getters and Setters

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

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getWorkSector() {
        return workSector;
    }

    public void setWorkSector(String workSector) {
        this.workSector = workSector;
    }

    public Integer getNrDependants() {
        return nrDependants;
    }

    public void setNrDependants(Integer nrDependants) {
        this.nrDependants = nrDependants;
    }

    public String getHomeOwnership() {
        return homeOwnership;
    }

    public void setHomeOwnership(String homeOwnership) {
        this.homeOwnership = homeOwnership;
    }

    public Boolean getHasMortgage() {
        return hasMortgage;
    }

    public void setHasMortgage(Boolean hasMortgage) {
        this.hasMortgage = hasMortgage;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getLtv() {
        return ltv;
    }

    public void setLtv(Double ltv) {
        this.ltv = ltv;
    }

    public Double getDti() {
        return dti;
    }

    public void setDti(Double dti) {
        this.dti = dti;
    }

    public Integer getPreviousLoansCount() {
        return previousLoansCount;
    }

    public void setPreviousLoansCount(Integer previousLoansCount) {
        this.previousLoansCount = previousLoansCount;
    }

    public Integer getPreviousDefaultsCount() {
        return previousDefaultsCount;
    }

    public void setPreviousDefaultsCount(Integer previousDefaultsCount) {
        this.previousDefaultsCount = previousDefaultsCount;
    }
}
