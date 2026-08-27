package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input;

import java.io.InputStream;
import java.net.URI;
import java.util.Objects;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.PageResponseDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.ReportPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller that exposes report-related endpoints.
 * Acts as the input adapter in the hexagonal architecture for the report
 * aggregate, receiving AI-generated risk reports from ms-reporting.
 *
 * @author Lucía Fernández Mancebo
 * @date 29/03/2026
 */
@Slf4j
@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Endpoints for AI-generated risk reports management")
public class ReportControllerAdapter {

        private final ReportPortService reportPortService;

        public ReportControllerAdapter(ReportPortService reportPortService) {
                this.reportPortService = Objects.requireNonNull(reportPortService);
        }

        /**
         * POST /api/reports
         * Persists a new AI-generated risk report produced by ms-reporting.
         *
         * @param dto the complete report data
         * @return 201 Created with the location of the new report
         */
        @PostMapping
        @Operation(summary = "Create a new report", description = "Persists a new AI-generated risk report produced by ms-reporting.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Report successfully created"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data")
        })
        public ResponseEntity<Void> createReport(
                        @Parameter(description = "The complete report data to create", required = true) @Valid @RequestBody CreateReportDTO dto) {

                log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "POST", "/api/reports");

                String reportId = reportPortService.createReport(dto);

                URI location = URI.create("/api/reports/" + reportId);

                log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 201, reportId);
                return ResponseEntity.created(location).build();
        }

        /**
         * GET /api/reports
         * Retrieves all stored reports with dynamic filtering and pagination.
         *
         * @param search  the generic search term (optional)
         * @param page    the page number (0-indexed)
         * @param size    the page size
         * @param sortBy  the field to sort by
         * @param sortDir the sort direction ("asc" or "desc")
         * @return 200 OK with the page of reports
         */
        @GetMapping
        @Operation(summary = "List all reports", description = "Retrieves all stored reports with dynamic filtering and pagination.")
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the page of reports")
        public ResponseEntity<PageResponseDTO<ReportDTO>> listReports(
                        @Parameter(description = "Generic search term for filtering reports") @RequestParam(required = false) String search,
                        @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Number of elements per page", example = "10") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "Field to sort by", example = "generationDate") @RequestParam(defaultValue = "generationDate") String sortBy,
                        @Parameter(description = "Sort direction (asc or desc)", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
                log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/reports");

                PageResponseDTO<ReportDTO> reports = reportPortService.listReports(search, page, size, sortBy, sortDir);

                log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS, 200, reports.getContent().size());
                return ResponseEntity.ok(reports);
        }

        /**
         * GET /api/reports?requestId={requestId}
         * Retrieves a stored report by request ID.
         *
         * @param requestId the ID of the request to filter by
         * @return 200 OK with the report
         */
        @GetMapping(params = "requestId")
        @Operation(summary = "Get report by request ID", description = "Retrieves a single stored report based on its associated request ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved the report"),
                        @ApiResponse(responseCode = "404", description = "Report not found for the given request ID")
        })
        public ResponseEntity<ReportDTO> getReportByRequestId(
                        @Parameter(description = "The unique identifier of the request", required = true) @RequestParam("requestId") String requestId) {
                log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/reports?requestId=" + requestId);

                ReportDTO report = reportPortService.getReportByRequestId(requestId);
                log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, report.getReportId());
                return ResponseEntity.ok(report);
        }

        /**
         * GET /api/reports/{reportId}/file
         * Retrieves the generated report PDF file.
         *
         * @param reportId the unique identifier of the report
         * @return the report file as a Resource
         */
        @GetMapping("/{reportId}/file")
        @Operation(summary = "Download report file", description = "Retrieves the generated report PDF file by its ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved the PDF file"),
                        @ApiResponse(responseCode = "404", description = "Report or file not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error reading the file")
        })
        public ResponseEntity<Resource> getReportFile(
                        @Parameter(description = "The unique identifier of the report", required = true) @PathVariable String reportId) {
                log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/reports/" + reportId + "/file");

                ReportDTO report = reportPortService.getReport(reportId);
                InputStream fileStream = reportPortService.getReportFileStream(report.getFilePath());

                String filename = report.getTitle() != null ? report.getTitle() + ".pdf"
                                : "report_" + reportId + ".pdf";

                log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, reportId);
                return ResponseEntity.ok()
                                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                                .header("Content-Type", MediaType.APPLICATION_PDF_VALUE)
                                .body(new InputStreamResource(fileStream));
        }
}
