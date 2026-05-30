package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.time.LocalDate;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractType;

/**
 * Abstract base class representing a financial contract.
 * Each subtype (Loan, Mortgage, CreditCard) implements its own monthly payment
 * calculation logic for DTI purposes, leveraging polymorphism instead of
 * switches.
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

    /**
     * Protected constructor to prevent direct instantiation of the abstract
     * Contract class.
     */
    protected Contract() {
    }

    /**
     * Constructor for Contract.
     * Subtypes will call this constructor to initialize common fields.
     * 
     * @param id           the unique identifier of the contract
     * @param contractType the type of the contract (LOAN, MORTGAGE, CREDIT_CARD)
     * @param status       the current status of the contract (ACTIVE, CLOSED,
     *                     DEFAULTED)
     * @param openDate     the date when the contract was opened
     * @param interestRate the interest rate applied to the contract (as a
     *                     percentage, e.g., 5.5 for 5.5%)
     */
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

    // toString, equals, and hashCode methods
    @Override
    public String toString() {
        return "Contract [id=" + id + ", contractType=" + contractType + ", status=" + status + ", openDate=" + openDate
                + ", interestRate=" + interestRate + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((contractType == null) ? 0 : contractType.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((openDate == null) ? 0 : openDate.hashCode());
        result = prime * result + ((interestRate == null) ? 0 : interestRate.hashCode());
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
        Contract other = (Contract) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (contractType != other.contractType)
            return false;
        if (status != other.status)
            return false;
        if (openDate == null) {
            if (other.openDate != null)
                return false;
        } else if (!openDate.equals(other.openDate))
            return false;
        if (interestRate == null) {
            if (other.interestRate != null)
                return false;
        } else if (!interestRate.equals(other.interestRate))
            return false;
        return true;
    }

}
