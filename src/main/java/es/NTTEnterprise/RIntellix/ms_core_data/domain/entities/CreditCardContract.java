package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractType;

import java.time.LocalDate;

/**
 * Entity representing a Credit Card contract (TARJETA_CREDITO).
 * Monthly payment calculation depends on the revolving mode:
 * - Non-revolving (is_revolving = false): M = currentBalance / 12
 *   (linear amortization at 0% interest over 12 months)
 * - Revolving (is_revolving = true): M = currentBalance × [ i(1+i)^12 / ((1+i)^12 − 1) ]
 *   (French system over 12 months with the card's annual interest rate)
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-02-2026
 */
public class CreditCardContract extends Contract {

    private static final int AMORTIZATION_MONTHS = 12;

    private Money creditLimit;
    private Money currentBalance;
    private Boolean isRevolving;

    public CreditCardContract() {
        super();
    }

    public CreditCardContract(String id, ContractStatus status, LocalDate openDate,
                              Double interestRate, Money creditLimit, Money currentBalance,
                              Boolean isRevolving) {
        super(id, ContractType.TARJETA_CREDITO, status, openDate, interestRate);
        this.creditLimit = creditLimit;
        this.currentBalance = currentBalance;
        this.isRevolving = isRevolving;
    }

    /**
     * Calculates the monthly payment for DTI based on the revolving mode.
     *
     * Non-revolving: Simple division of the current balance over 12 months (0% interest).
     * Formula: M = currentBalance / 12
     *
     * Revolving: French amortization of the current balance over 12 months at the card's interest rate.
     * Formula: M = currentBalance × [ i(1+i)^12 / ((1+i)^12 − 1) ]
     * Where: i = annual interest rate / (12 × 100)
     *
     * @return the monthly payment as a Money object
     */
    @Override
    public Money calculateMonthlyPayment() {

        Money monthlyPayment = new Money(0.0, currentBalance.getCurrency());
        double i = getInterestRate() / (12.0 * 100.0);
        if (!Boolean.TRUE.equals(isRevolving)) {
            monthlyPayment.setAmount(currentBalance.getAmount().doubleValue() / AMORTIZATION_MONTHS); 
        } else {
            if (i == 0) {
                monthlyPayment.setAmount(currentBalance.getAmount().doubleValue() / AMORTIZATION_MONTHS); 
            } else {
                double factor = Math.pow(1 + i, AMORTIZATION_MONTHS);
                double paymentAmount = currentBalance.getAmount().doubleValue() * (i * factor) / (factor - 1);
                monthlyPayment.setAmount(paymentAmount);
            }
        }
        return monthlyPayment;
    }


    @Override
    public Money getOutstandingDebt() {
        return currentBalance;
    }

    // Getters and Setters

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Money getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Money currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Boolean getIsRevolving() {
        return isRevolving;
    }

    public void setIsRevolving(Boolean isRevolving) {
        this.isRevolving = isRevolving;
    }
}
