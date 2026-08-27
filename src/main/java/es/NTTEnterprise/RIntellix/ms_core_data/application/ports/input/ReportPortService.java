package es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.FileStorageException;

import java.io.InputStream;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.PageResponseDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ReportDTO;

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
         * @param search  the generic search term (optional filter)
         * @param page    the page number (0-indexed)
         * @param size    the page size
         * @param sortBy  the field to sort by
         * @param sortDir the sort direction ("asc" or "desc")
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
         * @throws EntityNotFoundException  if
         *                                  not
         *                                  found
         * @throws IllegalArgumentException if
         *                                  reportId
         *                                  is
         *                                  null
         *                                  or
         *                                  blank
         */
        ReportDTO getReport(String reportId)
                        throws EntityNotFoundException,
                        IllegalArgumentException;

        /**
         * Retrieves the file content stream for a report.
         * Validates that the file path is present and delegates
         * file retrieval to the storage layer.
         *
         * @param filePath the path to the report file
         * @return an InputStream to read the report file content
         * @throws FileStorageException if the file path is missing or the file
         *                              cannot be retrieved
         */
        InputStream getReportFileStream(String filePath)
                        throws FileStorageException;
}
