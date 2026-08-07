package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output;

/**
 * Data Transfer Object (DTO) for detailed information about a request.
 * This class is used to transfer detailed data about a request from the
 * application layer to the presentation
 * layer, allowing to display all the relevant information about a specific
 * request without exposing the internal structure of the request entity.
 * 
 * @author Lucía Fernández Mancebo
 * @date 28/02/2026
 */
public class RequestDetailsDTO {

    // Amount and income are represented as String to preserve formatting at API
    // boundary.
    public String requestId;
    public String requestCode;
    public String requestDate;
    public String lastReviewDate;
    public String partyName;
    public String partyNIF;
    public String requestDescription;
    public String partyPhoneNumber;
    public String partyEmail;
    public String partyAddress;
    public String partyLaboralSituation;
    public Double partyIncome;
    public String requestType;
    public String status;
    public String currency;
    public Double requestedAmount;
    public Integer requestTermMonths;
    public Double interestRate;
    public String purpose;

    // Credit card specific fields
    public Double requestedCreditLimit;
    public Boolean isRevolving;

    // Mortgage specific fields
    public Double propertyValue;
    public Boolean isFirstHome;

    public RequestDetailsDTO() {
    }

    // Getters and Setters

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(String lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyNIF() {
        return partyNIF;
    }

    public void setPartyNIF(String partyNIF) {
        this.partyNIF = partyNIF;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getPartyPhoneNumber() {
        return partyPhoneNumber;
    }

    public void setPartyPhoneNumber(String partyPhoneNumber) {
        this.partyPhoneNumber = partyPhoneNumber;
    }

    public String getPartyEmail() {
        return partyEmail;
    }

    public void setPartyEmail(String partyEmail) {
        this.partyEmail = partyEmail;
    }

    public String getPartyAddress() {
        return partyAddress;
    }

    public void setPartyAddress(String partyAddress) {
        this.partyAddress = partyAddress;
    }

    public String getPartyLaboralSituation() {
        return partyLaboralSituation;
    }

    public void setPartyLaboralSituation(String partyLaboralSituation) {
        this.partyLaboralSituation = partyLaboralSituation;
    }

    public Double getPartyIncome() {
        return partyIncome;
    }

    public void setPartyIncome(Double partyIncome) {
        this.partyIncome = partyIncome;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public Integer getRequestTermMonths() {
        return requestTermMonths;
    }

    public void setRequestTermMonths(Integer requestTermMonths) {
        this.requestTermMonths = requestTermMonths;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    // Credit card specific getters and setters

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

    // Mortgage specific getters and setters

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

}
