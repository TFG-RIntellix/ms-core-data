package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;

/**
 * This class represents the details of a request who can be a loan, mortgage or a credit card. 
 * It contains the type of request, the purpose, the requested amount, the term in months, the interest rate, the credit limit (if applicable), whether it is revolving or not and the repayment system.
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
     * @param requestType the type of request, which can be a loan, mortgage or a credit card.
     * @param purpose the purpose of the request, which can be the purchase of a home, the improvement of a home, the purchase of a vehicle, home renovation, education, health, debt consolidation, appliances, technology, travel or other purposes.
     * @param requestedAmount the amount of money requested in the request, which is a Money object that contains the amount and the currency.
     * @param termMonths the term of the request in months, which can affect the interest rate and the monthly payment of the request.
     * @param interestRate the interest rate of the request, which can affect the total amount to be paid back and the monthly payment of the request.
     * @param creditLimit the credit limit of the request, which is only applicable for credit card requests and is a Money object that contains the amount and the currency.
     * @param isRevolving whether the request is revolving or not, which is only applicable for credit card requests and can affect the interest rate and the monthly payment of the request.
     * @param repaymentSystem the repayment system of the request, which can be fixed, variable or other types of repayment systems and can affect the monthly payment of the request.
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
}
