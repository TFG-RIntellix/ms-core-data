package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;

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

    /**
     * Retrieves reports, optionally filtering by requestId and scoringId.
     *
     * @param requestId the ID of the request (optional)
     * @param scoringId the ID of the scoring (optional)
     * @return a list of ReportDTO objects representing the reports
     */
    List<ReportDTO> listReports(String requestId, String scoringId);

    /**
     * Retrieves a report by its ID.
     *
     * @param reportId the ID of the report
     * @return the Report domain entity
     * @throws es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException if
     *                                                                                           not
     *                                                                                           found
     * @throws IllegalArgumentException                                                          if
     *                                                                                           reportId
     *                                                                                           is
     *                                                                                           null
     *                                                                                           or
     *                                                                                           blank
     */
    Report getReport(String reportId)
            throws es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException,
            IllegalArgumentException;
}
