package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output;

/**
 * Data Transfer Object (DTO) for detailed information about a request.
 * This class is used to transfer detailed data about a request from the application layer to the presentation
 * layer, allowing to display all the relevant information about a specific request without exposing the internal structure of the request entity.
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
public class RequestDetailsDTO {

    // TODO: Change the requested amount into a String and convert it when it is necessary, in order to avoid problems with the currency and the amount when it is sent to the presentation layer. The same for the income of the party.
    public String requestId;
    public String requestDate;
    public String partyName;
    public String partyNIF;
    public String requestDescription;
    public String partyPhoneNumber;
    public String partyEmail;
    public String partyAddress;
    public String partyLaboralSituation;
    public String partyIncome;
    public String requestType;
    public String status;
    public Double requestedAmount;
    public String currency;
    public Integer requestTermMonths;
    public Double interestRate;
    public String purpose;

    public RequestDetailsDTO() {
    }

    // Getters and Setters

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
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

    public String getPartyIncome() {
        return partyIncome;
    }

    public void setPartyIncome(String partyIncome) {
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

    public Double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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


}
