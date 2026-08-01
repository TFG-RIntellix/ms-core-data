package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.PagedResult;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;

import java.util.List;

/**
 * Output port for Report aggregate persistence.
 * Defines the repository contract for storing generated risk reports in the
 * "reports" collection.
 *
 * @author Lucía Fernández Mancebo
 * @date 29/03/2026
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
     * Retrieves report aggregates from the database with dynamic filtering and pagination.
     *
     * @param search     the generic search term (optional filter)
     * @param requestIds the list of request IDs to filter by (optional)
     * @param page       the page number (0-indexed)
     * @param size       the page size
     * @param sortBy     the field to sort by
     * @param sortDir    the sort direction ("asc" or "desc")
     * @return PagedResult of Report domain entities
     */
    PagedResult<Report> findWithFilters(
            String search, List<String> requestIds, int page, int size, String sortBy, String sortDir);

    /**
     * Retrieves a report by its associated request ID.
     *
     * @param requestId the ID of the associated request
     * @return the matching Report domain entity
     * @throws EntityNotFoundException if not found
     */
    Report findByRequestId(String requestId) throws EntityNotFoundException;

    /**
     * Retrieves a report by its ID.
     *
     * @param id the ID of the report
     * @return the matching Report domain entity
     * @throws EntityNotFoundException if not found
     * @throws IllegalArgumentException if id is null or invalid
     */
    Report findById(String id) throws EntityNotFoundException, IllegalArgumentException;
}
