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

    public LoanContract() {
        super();
    }


    public LoanContract(String id, ContractType contractType,ContractStatus status,
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
}
