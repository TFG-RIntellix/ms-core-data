package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Objects;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFactor;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ReportType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Severity;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.ReportPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ReportPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.CreateReportDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ReportDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Implements ReportPortService, handling the business logic for persisting
 * AI-generated risk reports received from ms-reporting.
 *
 * @author Lucía Fernández Mancebo
 * @date 29/03/2026
 */
@Slf4j
public class ReportApplicationService implements ReportPortService {

    private final ReportPortRepository reportPortRepository;
    private final CreateReportDTOMapper createReportDTOMapper;
    private final ReportDTOMapper reportDTOMapper;

    public ReportApplicationService(ReportPortRepository reportPortRepository,
            CreateReportDTOMapper createReportDTOMapper, ReportDTOMapper reportDTOMapper) {
        this.reportPortRepository = Objects.requireNonNull(reportPortRepository);
        this.createReportDTOMapper = Objects.requireNonNull(createReportDTOMapper);
        this.reportDTOMapper = Objects.requireNonNull(reportDTOMapper);
    }

    @Override
    public String createReport(CreateReportDTO dto) throws IllegalArgumentException {
        log.debug(LogMessage.SERVICE_CREATE_REPORT_START, dto.getRequestId(), dto.getScoringId());

        Report report = createReportDTOMapper.toDomain(dto);

        Report saved = reportPortRepository.save(report);
        log.debug(LogMessage.SERVICE_CREATE_REPORT_COMPLETE, saved.getId());

        return saved.getId();
    }

    @Override
    public List<ReportDTO> listReports() {
        log.debug(LogMessage.SERVICE_LIST_REPORTS_START);

        List<Report> reports = reportPortRepository.findAll();
        log.debug(LogMessage.SERVICE_LIST_REPORTS_RESULT, reports.size());

        return reports.stream()
                .map(reportDTOMapper::toDTO)
                .toList();
    }

    @Override
    public ReportDTO getReportByRequestId(String requestId) throws EntityNotFoundException {
        log.debug(LogMessage.SERVICE_GET_REPORT_REQ_START, requestId);

        Report report = reportPortRepository.findByRequestId(requestId);
        log.debug(LogMessage.SERVICE_GET_REPORT_REQ_COMPLETE);

        return reportDTOMapper.toDTO(report);
    }

    @Override
    public Report getReport(String reportId) throws EntityNotFoundException, IllegalArgumentException {

        log.debug(LogMessage.SERVICE_GET_REPORT_ID_START, reportId);

        if (reportId == null || reportId.isBlank()) {
            log.warn(LogMessage.SERVICE_GET_REPORT_ID_FAILED);
            throw new IllegalArgumentException("Report ID cannot be null or empty");
        }

        Report report = reportPortRepository.findById(reportId);
        log.debug(LogMessage.SERVICE_GET_REPORT_ID_COMPLETE, reportId);

        return report;
    }

}
