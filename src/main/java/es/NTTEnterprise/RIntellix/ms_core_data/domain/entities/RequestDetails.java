package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;

/**
 * This class represents the details of a request who can be a loan, mortgage or
 * a credit card.
 * It contains the type of request, the purpose, the requested amount, the term
 * in months, the interest rate, the credit limit (if applicable), whether it is
 * revolving or not and the repayment system.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
public class RequestDetails {

    private RequestType requestType;
    private Purpose purpose;
    private Money requestedAmount;
    private Integer termMonths;
    private Double interestRate;
    private Money creditLimit;
    private boolean isRevolving;
    private String repaymentSystem;

    /**
     * Constructor of the RequestDetails class.
     * 
     * @param requestType     the type of request, which can be a loan, mortgage or
     *                        a credit card.
     * @param purpose         the purpose of the request, which can be the purchase
     *                        of a home, the improvement of a home, the purchase of
     *                        a vehicle, home renovation, education, health, debt
     *                        consolidation, appliances, technology, travel or other
     *                        purposes.
     * @param requestedAmount the amount of money requested in the request, which is
     *                        a Money object that contains the amount and the
     *                        currency.
     * @param termMonths      the term of the request in months, which can affect
     *                        the interest rate and the monthly payment of the
     *                        request.
     * @param interestRate    the interest rate of the request, which can affect the
     *                        total amount to be paid back and the monthly payment
     *                        of the request.
     * @param creditLimit     the credit limit of the request, which is only
     *                        applicable for credit card requests and is a Money
     *                        object that contains the amount and the currency.
     * @param isRevolving     whether the request is revolving or not, which is only
     *                        applicable for credit card requests and can affect the
     *                        interest rate and the monthly payment of the request.
     * @param repaymentSystem the repayment system of the request, which can be
     *                        fixed, variable or other types of repayment systems
     *                        and can affect the monthly payment of the request.
     */
    public RequestDetails(RequestType requestType, Purpose purpose, Money requestedAmount, Integer termMonths,
            Double interestRate, Money creditLimit, boolean isRevolving, String repaymentSystem) {
        this.requestType = requestType;
        this.purpose = purpose;
        this.requestedAmount = requestedAmount;
        this.termMonths = termMonths;
        this.interestRate = interestRate;
        this.creditLimit = creditLimit;
        this.isRevolving = isRevolving;
        this.repaymentSystem = repaymentSystem;
    }

    // Getters and setters

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public Money getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(Money requestedAmount) {
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

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = creditLimit;
    }

    public boolean isRevolving() {
        return isRevolving;
    }

    public void setRevolving(boolean isRevolving) {
        this.isRevolving = isRevolving;
    }

    public String getRepaymentSystem() {
        return repaymentSystem;
    }

    public void setRepaymentSystem(String repaymentSystem) {
        this.repaymentSystem = repaymentSystem;
    }

    // toString, hashCode and equals
    @Override
    public String toString() {
        return "RequestDetails [requestType=" + requestType + ", purpose=" + purpose + ", requestedAmount="
                + requestedAmount + ", termMonths=" + termMonths + ", interestRate=" + interestRate + ", creditLimit="
                + creditLimit + ", isRevolving=" + isRevolving + ", repaymentSystem=" + repaymentSystem + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
        result = prime * result + ((purpose == null) ? 0 : purpose.hashCode());
        result = prime * result + ((requestedAmount == null) ? 0 : requestedAmount.hashCode());
        result = prime * result + ((termMonths == null) ? 0 : termMonths.hashCode());
        result = prime * result + ((interestRate == null) ? 0 : interestRate.hashCode());
        result = prime * result + ((creditLimit == null) ? 0 : creditLimit.hashCode());
        result = prime * result + (isRevolving ? 1231 : 1237);
        result = prime * result + ((repaymentSystem == null) ? 0 : repaymentSystem.hashCode());
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
        RequestDetails other = (RequestDetails) obj;
        if (requestType != other.requestType)
            return false;
        if (purpose != other.purpose)
            return false;
        if (requestedAmount == null) {
            if (other.requestedAmount != null)
                return false;
        } else if (!requestedAmount.equals(other.requestedAmount))
            return false;
        if (termMonths == null) {
            if (other.termMonths != null)
                return false;
        } else if (!termMonths.equals(other.termMonths))
            return false;
        if (interestRate == null) {
            if (other.interestRate != null)
                return false;
        } else if (!interestRate.equals(other.interestRate))
            return false;
        if (creditLimit == null) {
            if (other.creditLimit != null)
                return false;
        } else if (!creditLimit.equals(other.creditLimit))
            return false;
        if (isRevolving != other.isRevolving)
            return false;
        if (repaymentSystem == null) {
            if (other.repaymentSystem != null)
                return false;
        } else if (!repaymentSystem.equals(other.repaymentSystem))
            return false;
        return true;
    }
}
