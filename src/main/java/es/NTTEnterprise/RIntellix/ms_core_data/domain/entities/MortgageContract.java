package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.time.LocalDate;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Entity representing a Mortgage contract (HIPOTECA).
 * Extends LoanContract since a mortgage IS-A loan with identical payment logic
 * and additional property-related fields (propertyValue, isFirstHome).
 *
 * Inherits calculateMonthlyPayment() and getOutstandingDebt() from LoanContract
 * (monthly payment comes directly from the database).
 * Adds LTV (Loan-To-Value) calculation specific to mortgages.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-02-2026
 */
@Slf4j
public class MortgageContract extends LoanContract {

    private Money propertyValue;
    private Boolean isFirstHome;

    /**
     * Default constructor for MortgageContract.
     */
    public MortgageContract() {
        super();
    }

    /**
     * Parameterized constructor for MortgageContract.
     * Initializes all fields including those inherited from LoanContract.
     * 
     * @param id                 unique identifier of the contract
     * @param status             status of the contract (ACTIVE, CLOSED, DEFAULTED)
     * @param openDate           date when the contract was opened
     * @param interestRate       annual interest rate of the loan
     * @param principalAmount    initial amount of the loan
     * @param termMonths         duration of the loan in months
     * @param monthlyPayment     fixed monthly payment amount
     * @param outstandingBalance current outstanding balance of the loan
     * @param purpose            purpose of the loan (e.g., HOME_PURCHASE,
     *                           REFINANCE)
     * @param propertyValue      current value of the mortgaged property
     * @param isFirstHome        indicates if the property is the borrower's first
     *                           home (true/false)
     */
    public MortgageContract(String id, ContractStatus status, LocalDate openDate,
            Double interestRate, Money principalAmount, Integer termMonths,
            Money monthlyPayment, Money outstandingBalance, Purpose purpose,
            Money propertyValue, Boolean isFirstHome) {
        super(id, ContractType.HIPOTECA, status, openDate, interestRate,
                principalAmount, termMonths, monthlyPayment, outstandingBalance, purpose);
        this.propertyValue = propertyValue;
        this.isFirstHome = isFirstHome;
    }

    /**
     * Calculates the Loan-To-Value (LTV) ratio for this mortgage.
     * LTV = Outstanding Balance / Property Value
     *
     * @return the LTV ratio as a Double, or null if property value is not available
     */
    public Double getLTV() {

        Double propertyValueAmount = (getPropertyValue() != null) ? getPropertyValue().getAmount().doubleValue() : null;
        Double outstandingBalanceAmount = (getOutstandingBalance() != null)
                ? getOutstandingBalance().getAmount().doubleValue()
                : null;

        if (propertyValueAmount == null || propertyValueAmount == 0 || outstandingBalanceAmount == null) {
            log.debug(LogMessage.DOMAIN_LTV_NO_DATA);
            return null;
        }
        Double ltv = outstandingBalanceAmount / propertyValueAmount;
        log.debug(LogMessage.DOMAIN_LTV_RESULT, outstandingBalanceAmount, propertyValueAmount, ltv);
        return ltv;
    }

    // Getters and Setters

    public Money getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Money propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Boolean getIsFirstHome() {
        return isFirstHome;
    }

    public void setIsFirstHome(Boolean isFirstHome) {
        this.isFirstHome = isFirstHome;
    }

    // toString, hashCode and equals
    @Override
    public String toString() {
        return "MortgageContract [propertyValue=" + propertyValue + ", isFirstHome=" + isFirstHome + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((propertyValue == null) ? 0 : propertyValue.hashCode());
        result = prime * result + ((isFirstHome == null) ? 0 : isFirstHome.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        MortgageContract other = (MortgageContract) obj;
        if (propertyValue == null) {
            if (other.propertyValue != null)
                return false;
        } else if (!propertyValue.equals(other.propertyValue))
            return false;
        if (isFirstHome == null) {
            if (other.isFirstHome != null)
                return false;
        } else if (!isFirstHome.equals(other.isFirstHome))
            return false;
        return true;
    }

}
