package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.time.LocalDate;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;

/**
 * Entity representing a fixed-interest Loan contract (PRESTAMO).
 * Also serves as base class for MortgageContract, since a mortgage IS-A loan
 * with additional property-related fields.
 *
 * The monthly payment value is stored directly in the database (pre-calculated
 * using the French amortization system at origination time), so
 * calculateMonthlyPayment() simply returns that stored value.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-02-2026
 */
public class LoanContract extends Contract {

    private Money principalAmount;
    private Integer termMonths;
    private Money monthlyPayment;
    private Money outstandingBalance;
    private Purpose purpose;

    /**
     * Default constructor for LoanContract.
     */

    public LoanContract() {
        super();
    }

    /**
     * Constructor for LoanContract.
     * 
     * @param id                 the unique identifier of the contract
     * @param contractType       the type of the contract (should be
     *                           ContractType.PRESTAMO for this class)
     * @param status             the status of the contract
     * @param openDate           the date the contract was opened
     * @param interestRate       the interest rate of the contract
     * @param principalAmount    the principal amount of the loan
     * @param termMonths         the term of the loan in months
     * @param monthlyPayment     the monthly payment amount
     * @param outstandingBalance the outstanding balance of the loan
     * @param purpose            the purpose of the loan
     */
    public LoanContract(String id, ContractType contractType, ContractStatus status,
            LocalDate openDate, Double interestRate,
            Money principalAmount, Integer termMonths, Money monthlyPayment,
            Money outstandingBalance, Purpose purpose) {
        super(id, contractType, status, openDate, interestRate);
        this.principalAmount = principalAmount;
        this.termMonths = termMonths;
        this.monthlyPayment = monthlyPayment;
        this.outstandingBalance = outstandingBalance;
        this.purpose = purpose;
    }

    // Getters and Setters

    public void setPrincipalAmount(Money principalAmount) {
        this.principalAmount = principalAmount;
    }

    public Money getPrincipalAmount() {
        return principalAmount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public Money getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(Money monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public Money getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(Money outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    @Override
    public Money calculateMonthlyPayment() {
        return monthlyPayment;
    }

    @Override
    public Money getOutstandingDebt() {
        return outstandingBalance;
    }

    // toString, hashCode and equals
    @Override
    public String toString() {
        return "LoanContract [principalAmount=" + principalAmount + ", termMonths=" + termMonths + ", monthlyPayment="
                + monthlyPayment + ", outstandingBalance=" + outstandingBalance + ", purpose=" + purpose + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((principalAmount == null) ? 0 : principalAmount.hashCode());
        result = prime * result + ((termMonths == null) ? 0 : termMonths.hashCode());
        result = prime * result + ((monthlyPayment == null) ? 0 : monthlyPayment.hashCode());
        result = prime * result + ((outstandingBalance == null) ? 0 : outstandingBalance.hashCode());
        result = prime * result + ((purpose == null) ? 0 : purpose.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LoanContract other = (LoanContract) obj;
        if (principalAmount == null) {
            if (other.principalAmount != null)
                return false;
        } else if (!principalAmount.equals(other.principalAmount))
            return false;
        if (termMonths == null) {
            if (other.termMonths != null)
                return false;
        } else if (!termMonths.equals(other.termMonths))
            return false;
        if (monthlyPayment == null) {
            if (other.monthlyPayment != null)
                return false;
        } else if (!monthlyPayment.equals(other.monthlyPayment))
            return false;
        if (outstandingBalance == null) {
            if (other.outstandingBalance != null)
                return false;
        } else if (!outstandingBalance.equals(other.outstandingBalance))
            return false;
        if (purpose != other.purpose)
            return false;
        return true;
    }

}
