package es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.PageResponseDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;

/**
 * Input port for report operations.
 * Defines the use case of persisting an AI-generated risk report received from
 * ms-reporting.
 *
 * @author Lucía Fernández Mancebo
 * @date 29/03/2026
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
     * Retrieves all reports with dynamic filtering and pagination.
     *
     * @param search   the generic search term (optional filter)
     * @param page     the page number (0-indexed)
     * @param size     the page size
     * @param sortBy   the field to sort by
     * @param sortDir  the sort direction ("asc" or "desc")
     * @return a PageResponseDTO of ReportDTO objects
     */
    PageResponseDTO<ReportDTO> listReports(
            String search, int page, int size, String sortBy, String sortDir);

    /**
     * Retrieves a report by its associated request ID.
     *
     * @param requestId the ID of the request
     * @return the ReportDTO object representing the report
     * @throws EntityNotFoundException if not found
     */
    ReportDTO getReportByRequestId(String requestId) throws EntityNotFoundException;

    /**
     * Retrieves a report by its ID.
     *
     * @param reportId the ID of the report
     * @return the Report domain entity
     * @throws EntityNotFoundException if
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
            throws EntityNotFoundException,
            IllegalArgumentException;
}
