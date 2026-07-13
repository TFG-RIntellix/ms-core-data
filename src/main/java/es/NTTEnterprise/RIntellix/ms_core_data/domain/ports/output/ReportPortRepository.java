package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;

/**
 * Output port for Report aggregate persistence.
 * Defines the repository contract for storing generated risk reports in the
 * "reports" collection.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-29-2026
 */
public interface ReportPortRepository {

    /**
     * Persists a report aggregate to the database.
     *
     * @param report the Report domain entity to save
     * @return the saved Report domain entity with its assigned ID
     */
    Report save(Report report);
    /**
     * Retrieves all report aggregates from the database.
     *
     * @return a list of all Report domain entities
     */
    List<Report> findAll();

    /**
     * Retrieves report aggregates with optional filters.
     *
     * @param requestId the ID of the associated request (optional filter)
     * @param scoringId the ID of the associated scoring (optional filter)
     * @return a list of matching Report domain entities
     */
    List<Report> findWithFilters(String requestId, String scoringId);

    /**
     * Retrieves a report by its ID.
     *
     * @param id the ID of the report
     * @return the matching Report domain entity
     * @throws es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException if not found
     * @throws IllegalArgumentException if id is null or invalid
     */
    Report findById(String id) throws es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException, IllegalArgumentException;
}
