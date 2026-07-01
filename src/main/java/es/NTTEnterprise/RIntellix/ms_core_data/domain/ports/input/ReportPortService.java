package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateReportDTO;

/**
 * Input port for report operations.
 * Defines the use case of persisting an AI-generated risk report received from
 * ms-reporting.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-29-2026
 */
public interface ReportPortService {

    /**
     * Creates and persists a new report with the complete data provided by
     * ms-reporting.
     *
     * @param dto the complete report data
     * @return the ID of the newly created report
     * @throws IllegalArgumentException if required fields are missing or invalid
     */
    String createReport(CreateReportDTO dto) throws IllegalArgumentException;
}
