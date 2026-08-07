package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities;

import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

    @Field("request_type")
    private RequestType requestType;

    @Field("request_code")
    private String requestCode;

    @Field("party_id")
    private ObjectId partyId;

    @Field("currency")
    private String currency;

    @Field("status")
    private RequestStatus status;

    @Field("request_date")
    private LocalDate requestDate;

    @Field("last_review_date")
    private LocalDate lastReviewDate;

    @Field("requested_amount")
    private Double requestedAmount;

    @Field("requested_interest_rate")
    private Double requestedInterestRate;

    @Field("requested_term_months")
    private Integer requestedTermMonths;

    @Field("purpose")
    private Purpose purpose;

    @Field("property_value")
    private Double propertyValue;

    @Field("is_first_home")
    private Boolean isFirstHome;

    @Field("requested_credit_limit")
    private Double requestedCreditLimit;

    @Field("is_revolving")
    private Boolean isRevolving;

    @Field("repayment_system")
    private String repaymentSystem;

    @Field("loan_type")
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

    public Double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public Double getRequestedInterestRate() {
        return requestedInterestRate;
    }

    public void setRequestedInterestRate(Double requestedInterestRate) {
        this.requestedInterestRate = requestedInterestRate;
    }

    public Integer getRequestedTermMonths() {
        return requestedTermMonths;
    }

    public void setRequestedTermMonths(Integer requestedTermMonths) {
        this.requestedTermMonths = requestedTermMonths;
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

    public Double getRequestedCreditLimit() {
        return requestedCreditLimit;
    }

    public void setRequestedCreditLimit(Double requestedCreditLimit) {
        this.requestedCreditLimit = requestedCreditLimit;
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
