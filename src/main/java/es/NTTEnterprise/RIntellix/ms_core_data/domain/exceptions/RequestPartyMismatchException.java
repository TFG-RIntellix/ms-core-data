package es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions;

/**
 * Thrown when a simulation references a requestId that does not belong
 * to the specified partyId. This enforces consistency between the
 * request and the party associated with a simulation.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-08-2026
 */
public class RequestPartyMismatchException extends RuntimeException {

    public RequestPartyMismatchException(String message) {
        super(message);
    }
}
