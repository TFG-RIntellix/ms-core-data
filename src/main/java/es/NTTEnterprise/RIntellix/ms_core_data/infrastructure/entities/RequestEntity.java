package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities;

import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;

/**
 * Entity representing a financial product request in MongoDB.
 * Central point for risk analysis.
 * 
 * @author Lucía Fernández Mancebo
 * @date 28/02/2026
 */
@Document(collection = "requests")
public class RequestEntity {

    @Id
    private ObjectId id;
    private RequestType requestType;
    private String requestCode;
    private ObjectId partyId;
    private String currency;
    private RequestStatus status;
    private LocalDate requestDate;
    private LocalDate lastReviewDate;
    private Double loanAmount;
    private Double interestRate;
    private Integer termMonths;
    private Purpose purpose;
    private Double propertyValue;
    private Boolean isFirstHome;
    private Double creditLimit;
    private Boolean isRevolving;
    private String repaymentSystem;
    private String loanType;

    public RequestEntity() {
    }

    // Getters and Setters

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public ObjectId getPartyId() {
        return partyId;
    }

    public void setPartyId(ObjectId partyId) {
        this.partyId = partyId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDate getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(LocalDate lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
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

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
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

    public Boolean getIsRevolving() {
        return isRevolving;
    }

    public void setIsRevolving(Boolean isRevolving) {
        this.isRevolving = isRevolving;
    }

    public String getRepaymentSystem() {
        return repaymentSystem;
    }

    public void setRepaymentSystem(String repaymentSystem) {
        this.repaymentSystem = repaymentSystem;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
}

