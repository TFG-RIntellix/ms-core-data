package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.List;
import java.util.Objects;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFactor;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ReportType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Severity;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.ReportPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ReportPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.CreateReportDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Implements ReportPortService, handling the business logic for persisting
 * AI-generated risk reports received from ms-reporting.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-29-2026
 */
@Slf4j
public class ReportApplicationService implements ReportPortService {

    private final ReportPortRepository reportPortRepository;
    private final CreateReportDTOMapper createReportDTOMapper;

    public ReportApplicationService(ReportPortRepository reportPortRepository, CreateReportDTOMapper createReportDTOMapper) {
        this.reportPortRepository = Objects.requireNonNull(reportPortRepository);
        this.createReportDTOMapper = Objects.requireNonNull(createReportDTOMapper);
    }

    @Override
    public String createReport(CreateReportDTO dto) throws IllegalArgumentException {
        log.debug(LogMessage.SERVICE_CREATE_REPORT_START, dto.getRequestId(), dto.getScoringId());

        Report report = createReportDTOMapper.toDomain(dto);

        Report saved = reportPortRepository.save(report);
        log.debug(LogMessage.SERVICE_CREATE_REPORT_COMPLETE, saved.getId());

        return saved.getId();
    }
}

