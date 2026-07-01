package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output;

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
}
