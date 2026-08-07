package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output;

/**
 * Data Transfer Object (DTO) class that represents a summary of a request,
 * containing only the most relevant information about the request.
 * This class is used to transfer data between the application layer and the
 * presentation layer, allowing to display a summary of the request without
 * exposing all the details of the request entity.
 * 
 * @author Lucía Fernández Mancebo
 * @date 28/02/2026
 */
public class RequestSummaryDTO {

    private String requestId;
    private String requestCode;
    private String partyName;
    private String status;
    private String requestType;
    private Double amount;
    private String currency;
    private String creationDate;
    private String lastReviewDate;

    /**
     * Constructor for the RequestSummaryDTO class, which initializes all the fields
     * of the DTO with the provided values.
     * 
     * @param requestId      the unique identifier of the request
     * @param partyName      the name of the party associated with the request
     * @param status         the current status of the request (e.g., "Pendiente de
     *                       Revision", "Revisado", "Aprobado", "Rechazado")
     * @param requestType    the type of the request (e.g., "Solicitud de Crédito",
     *                       "Solicitud de Pago", "Solicitud de Devolución")
     * @param amount         the amount of money involved in the request
     * @param currency       the currency of the amount (e.g., "USD", "EUR", "GBP")
     * @param creationDate   the date when the request was created
     * @param lastReviewDate the date when the request was last reviewed
     */
    public RequestSummaryDTO(String requestId, String partyName, String status, String requestType, Double amount,
            String currency, String creationDate, String lastReviewDate) {
        this.requestId = requestId;
        this.partyName = partyName;
        this.status = status;
        this.requestType = requestType;
        this.amount = amount;
        this.currency = currency;
        this.creationDate = creationDate;
        this.lastReviewDate = lastReviewDate;
    }

    /**
     * Default constructor for the RequestSummaryDTO class, which initializes all
     * the fields of the DTO with default values (null or zero).
     */
    public RequestSummaryDTO() {
    }

    // Getters and setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
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
}
