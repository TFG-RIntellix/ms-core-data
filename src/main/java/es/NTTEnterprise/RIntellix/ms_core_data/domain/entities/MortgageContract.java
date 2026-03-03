package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

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

    public MortgageContract() {
        super();
    }

    public MortgageContract(String id, ContractStatus status, LocalDate openDate,
                            Double interestRate,  Money principalAmount, Integer termMonths,
                            Money monthlyPayment,  Money outstandingBalance, Purpose purpose,
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
        Double outstandingBalanceAmount = (getOutstandingBalance() != null) ? getOutstandingBalance().getAmount().doubleValue() : null;

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
}
