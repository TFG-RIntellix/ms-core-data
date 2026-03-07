package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.util.Date;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestStatus;

/**
 * This class represents a request, which can be a loan, mortgage or a credit
 * card.
 * It contains the creation date, the collateral (if applicable), the details of
 * the request and the status of the request.
 *
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
public class Request {

    private String id;
    private String partyId; // Reference to Party by ID (for lazy loading)
    private Date creationDate;
    private Date lastReviewDate;
    private PropertyCollateral collateral;
    private RequestDetails requestDetails;
    private RequestStatus requestStatus;
    private Party party; // Association to Party (populated when needed)

    /**
     * Constructor of the Request class.
     * 
     * @param collateral     the collateral of the request, which can be null if the
     *                       request is not a mortgage.
     * @param creationDate   the date of creation of the request.
     * @param requestDetails the details of the request, which contains the type of
     *                       request, the purpose, the requested amount, the term in
     *                       months, the interest rate, the credit limit (if
     *                       applicable), whether it is revolving or not and the
     *                       repayment system.
     * @param requestStatus  the status of the request, which can be a loan,
     *                       mortgage or a credit card.
     */
    public Request(PropertyCollateral collateral, Date creationDate, RequestDetails requestDetails,
            RequestStatus requestStatus) {
        this.collateral = collateral;
        this.creationDate = creationDate;
        this.requestDetails = requestDetails;
        this.requestStatus = requestStatus;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public PropertyCollateral getCollateral() {
        return collateral;
    }

    public void setCollateral(PropertyCollateral collateral) {
        this.collateral = collateral;
    }

    public RequestDetails getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(RequestDetails requestDetails) {
        this.requestDetails = requestDetails;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(Date lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    // toString, hashCode and equals methods

    @Override
    public String toString() {
        return "Request [creationDate=" + creationDate + ", collateral=" + collateral + ", requestDetails="
                + requestDetails + ", requestStatus=" + requestStatus + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((collateral == null) ? 0 : collateral.hashCode());
        result = prime * result + ((requestDetails == null) ? 0 : requestDetails.hashCode());
        result = prime * result + ((requestStatus == null) ? 0 : requestStatus.hashCode());
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
        Request other = (Request) obj;
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        if (collateral == null) {
            if (other.collateral != null)
                return false;
        } else if (!collateral.equals(other.collateral))
            return false;
        if (requestDetails == null) {
            if (other.requestDetails != null)
                return false;
        } else if (!requestDetails.equals(other.requestDetails))
            return false;
        if (requestStatus != other.requestStatus)
            return false;
        return true;
    }

}
