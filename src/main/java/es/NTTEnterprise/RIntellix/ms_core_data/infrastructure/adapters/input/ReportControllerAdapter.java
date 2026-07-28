package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.ReportPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller that exposes report-related endpoints.
 * Acts as the input adapter in the hexagonal architecture for the report
 * aggregate, receiving AI-generated risk reports from ms-reporting.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-29-2026
 */
@Slf4j
@RestController
@RequestMapping("/api/reports")
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
    public ResponseEntity<Void> createReport(@Valid @RequestBody CreateReportDTO dto) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "POST", "/api/reports");

        String reportId = reportPortService.createReport(dto);

        URI location = URI.create("/api/reports/" + reportId);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 201, reportId);
        return ResponseEntity.created(location).build();
    }

    /**
     * GET /api/reports
     * Retrieves all stored reports.
     *
     * @return 200 OK with the list of reports
     */
    @GetMapping
    public ResponseEntity<List<ReportDTO>> listReports() {
        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/reports");

        List<ReportDTO> reports = reportPortService.listReports();

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS, 200, reports.size());
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
    public ResponseEntity<ReportDTO> getReportByRequestId(
            @RequestParam("requestId") String requestId) {
        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/reports?requestId=" + requestId);

        try {
            ReportDTO report = reportPortService.getReportByRequestId(requestId);
            log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, report.getReportId());
            return ResponseEntity.ok(report);
        } catch (EntityNotFoundException e) {
            log.warn(LogMessage.CONTROLLER_REPORT_NOT_FOUND_REQ, requestId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/reports/{reportId}/file
     * Retrieves the generated report PDF file.
     *
     * @param reportId the unique identifier of the report
     * @return the report file as a Resource
     */
    @GetMapping("/{reportId}/file")
    public ResponseEntity<Resource> getReportFile(@PathVariable String reportId) {
        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/reports/" + reportId + "/file");

        Report report = reportPortService.getReport(reportId);
        String filePath = report.getFilePath();

        if (filePath == null || filePath.isBlank()) {
            log.warn(LogMessage.CONTROLLER_REPORT_NO_FILE, reportId);
            return ResponseEntity.notFound().build();
        }

        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                String filename = report.getTitle() != null ? report.getTitle() + ".pdf"
                        : "report_" + reportId + ".pdf";

                log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, reportId);
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                        .header("Content-Type", MediaType.APPLICATION_PDF_VALUE)
                        .body(resource);
            } else {
                log.warn(LogMessage.CONTROLLER_REPORT_FILE_NOT_READABLE, filePath, reportId);
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            log.error(LogMessage.CONTROLLER_REPORT_URL_ERROR, filePath, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
