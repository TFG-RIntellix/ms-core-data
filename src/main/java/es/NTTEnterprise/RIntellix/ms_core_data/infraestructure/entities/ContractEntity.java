package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Infrastructure entity mapping the "contracts" MongoDB collection.
 * Uses a single document structure (union of all contract-type fields)
 * since MongoDB stores all contract types in the same collection with
 * discriminated fields per contract_type.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-02-2026
 */
@Document(collection = "contracts")
public class ContractEntity {

    @Id
    private String id;

    @Field("contract_type")
    private String contractType;

    @Field("party_id")
    private String partyId;

    @Field("currency")
    private String currency;

    @Field("status")
    private String status;

    @Field("open_date")
    private Date openDate;

    // --- Loan / Mortgage fields ---

    @Field("purpose")
    private String purpose;

    @Field("principal_amount")
    private Double principalAmount;

    @Field("interest_rate")
    private Double interestRate;

    @Field("term_months")
    private Integer termMonths;

    @Field("monthly_payment")
    private Double monthlyPayment;

    @Field("outstanding_balance")
    private Double outstandingBalance;

    // --- Mortgage-specific fields ---

    @Field("property_value")
    private Double propertyValue;

    @Field("is_first_home")
    private Boolean isFirstHome;

    // --- Credit Card fields ---

    @Field("credit_limit")
    private Double creditLimit;

    @Field("current_balance")
    private Double currentBalance;

    @Field("is_revolving")
    private Boolean isRevolving;

    public ContractEntity() {
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Double getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(Double principalAmount) {
        this.principalAmount = principalAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public Double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(Double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public Double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(Double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public Double getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Double propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Boolean getIsFirstHome() {
        return isFirstHome;
    }

    public void setIsFirstHome(Boolean isFirstHome) {
        this.isFirstHome = isFirstHome;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Boolean getIsRevolving() {
        return isRevolving;
    }

    public void setIsRevolving(Boolean isRevolving) {
        this.isRevolving = isRevolving;
    }
}
