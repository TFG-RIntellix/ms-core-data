package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractType;

import java.time.LocalDate;

/**
 * Abstract base class representing a financial contract.
 * Each subtype (Loan, Mortgage, CreditCard) implements its own monthly payment
 * calculation logic for DTI purposes, leveraging polymorphism instead of switches.
 * Contracts are entities within the Party aggregate, accessed through Person.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-02-2026
 */
public abstract class Contract {

    private String id;
    private ContractType contractType;
    private ContractStatus status;
    private LocalDate openDate;
    private Double interestRate;

    protected Contract() {
    }

    protected Contract(String id, ContractType contractType, ContractStatus status,
         LocalDate openDate, Double interestRate) {
        this.id = id;
        this.contractType = contractType;
        this.status = status;
        this.openDate = openDate;
        this.interestRate = interestRate;
    }

    /**
     * Calculates the estimated monthly payment contribution for DTI calculation.
     * Each contract subtype implements its own formula:
     * - Loan/Mortgage: French amortization system (cuota constante)
     * - CreditCard Non-Revolving: currentBalance / 12
     * - CreditCard Revolving: French system over 12 months with interest
     *
     * @return the monthly payment as a Money object
     */
    public abstract Money calculateMonthlyPayment();

    /**
     * Returns the outstanding debt balance for this contract.
     * Used by Person.getTotalDebt() to sum all active debts.
     *
     * @return the outstanding debt as a Money object
     */
    public abstract Money getOutstandingDebt();

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }
}
