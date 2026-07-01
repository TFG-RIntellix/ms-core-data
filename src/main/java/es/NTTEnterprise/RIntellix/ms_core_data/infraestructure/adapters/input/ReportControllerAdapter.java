package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.input;

import java.net.URI;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.ReportPortService;
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
}
