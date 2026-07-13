package es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions;

/**
 * Exception thrown when attempting to create a simulation with a scenario name
 * that already exists for the same request.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-08-2026
 */
public class DuplicateSimulationNameException extends RuntimeException {

    public DuplicateSimulationNameException(String message) {
        super(message);
    }

}
