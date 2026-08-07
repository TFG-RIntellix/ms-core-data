package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output;

/**
 * Lightweight internal DTO exposing only the party identifiers associated with a
 * request. Intended for service-to-service consumers (e.g. ms-reporting) that
 * need to reference the party without pulling the full request/party detail that
 * the frontend-facing {@link RequestDetailsDTO} carries.
 *
 * @author Lucía Fernández Mancebo
 */
public class RequestPartyDTO {

    private String requestId;
    private String partyId;
    private String partyName;

    public RequestPartyDTO() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

}
