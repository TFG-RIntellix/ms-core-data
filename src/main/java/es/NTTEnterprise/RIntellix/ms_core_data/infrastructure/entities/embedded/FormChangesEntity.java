package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded entity mapping the "form_changes" sub-document from the
 * "simulations" MongoDB collection.
 * Contains the user-modified input values for the simulation scenario.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public class FormChangesEntity {

    @Field("annual_income")
    private Double annualIncome;

    @Field("term_months")
    private Integer termMonths;

    @Field("loan_amount")
    private Double loanAmount;

    @Field("interest_rate")
    private Double interestRate;

    @Field("nr_dependants")
    private Integer nrDependants;

    @Field("repayment_system")
    private String repaymentSystem;

    @Field("employment_status")
    private String employmentStatus;

    @Field("is_revolving")
    private Boolean isRevolving;

    @Field("property_value")
    private Double propertyValue;

    @Field("credit_limit")
    private Double creditLimit;

    public FormChangesEntity() {
    }

    // Getters and Setters

    public Double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getNrDependants() {
        return nrDependants;
    }

    public void setNrDependants(Integer nrDependants) {
        this.nrDependants = nrDependants;
    }

    public String getRepaymentSystem() {
        return repaymentSystem;
    }

    public void setRepaymentSystem(String repaymentSystem) {
        this.repaymentSystem = repaymentSystem;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Boolean getIsRevolving() {
        return isRevolving;
    }

    public void setIsRevolving(Boolean isRevolving) {
        this.isRevolving = isRevolving;
    }

    public Double getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Double propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }
}
