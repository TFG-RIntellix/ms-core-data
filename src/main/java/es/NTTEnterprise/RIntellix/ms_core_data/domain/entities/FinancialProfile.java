package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.EmploymentStatus;

/**
 * Value object representing the financial profile of a person.
 * Contains income, employment and credit history information.
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public class FinancialProfile {

    private Money annualIncome;
    private EmploymentStatus employmentStatus;
    private String occupation;
    private Double seniorityYears;

    // Credit History
    private Integer previousDefaultsCount;
    private Money previousRepaymentsAmount;
    private Integer previousLoansCount;
    private Money previousLoansAmount;
    private Boolean isNewCustomer;

    public FinancialProfile() {
    }

    public FinancialProfile(Money annualIncome, EmploymentStatus employmentStatus, String occupation,
                           Double seniorityYears, Integer previousDefaultsCount, Money previousRepaymentsAmount,
                           Integer previousLoansCount, Money previousLoansAmount, Boolean isNewCustomer) {
        this.annualIncome = annualIncome;
        this.employmentStatus = employmentStatus;
        this.occupation = occupation;
        this.seniorityYears = seniorityYears;
        this.previousDefaultsCount = previousDefaultsCount;
        this.previousRepaymentsAmount = previousRepaymentsAmount;
        this.previousLoansCount = previousLoansCount;
        this.previousLoansAmount = previousLoansAmount;
        this.isNewCustomer = isNewCustomer;
    }

    // Getters and Setters

    public Money getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Money annualIncome) {
        this.annualIncome = annualIncome;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Double getSeniorityYears() {
        return seniorityYears;
    }

    public void setSeniorityYears(Double seniorityYears) {
        this.seniorityYears = seniorityYears;
    }

    public Integer getPreviousDefaultsCount() {
        return previousDefaultsCount;
    }

    public void setPreviousDefaultsCount(Integer previousDefaultsCount) {
        this.previousDefaultsCount = previousDefaultsCount;
    }

    public Money getPreviousRepaymentsAmount() {
        return previousRepaymentsAmount;
    }

    public void setPreviousRepaymentsAmount(Money previousRepaymentsAmount) {
        this.previousRepaymentsAmount = previousRepaymentsAmount;
    }

    public Integer getPreviousLoansCount() {
        return previousLoansCount;
    }

    public void setPreviousLoansCount(Integer previousLoansCount) {
        this.previousLoansCount = previousLoansCount;
    }

    public Money getPreviousLoansAmount() {
        return previousLoansAmount;
    }

    public void setPreviousLoansAmount(Money previousLoansAmount) {
        this.previousLoansAmount = previousLoansAmount;
    }

    public Boolean getIsNewCustomer() {
        return isNewCustomer;
    }

    public void setIsNewCustomer(Boolean isNewCustomer) {
        this.isNewCustomer = isNewCustomer;
    }
}
