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

    public ReportApplicationService(ReportPortRepository reportPortRepository) {
        this.reportPortRepository = Objects.requireNonNull(reportPortRepository);
    }

    @Override
    public String createReport(CreateReportDTO dto) throws IllegalArgumentException {
        log.debug(LogMessage.SERVICE_CREATE_REPORT_START, dto.getRequestId(), dto.getScoringId());

        Report report = buildReport(dto);

        Report saved = reportPortRepository.save(report);
        log.debug(LogMessage.SERVICE_CREATE_REPORT_COMPLETE, saved.getId());

        return saved.getId();
    }

    /**
     * Builds a Report domain entity from the CreateReportDTO, parsing the
     * report type and risk factor severities into their domain enums.
     *
     * @param dto the creation DTO with the complete report data
     * @return a fully populated Report domain entity ready for persistence
     * @throws IllegalArgumentException if the report type or a severity value is
     *                                  not a valid enum constant
     */
    private Report buildReport(CreateReportDTO dto) {
        Report report = new Report();

        report.setPartyId(dto.getPartyId());
        report.setRequestId(dto.getRequestId());
        report.setScoringId(dto.getScoringId());
        report.setReportType(parseReportType(dto.getReportType()));
        report.setTitle(dto.getTitle());
        report.setAiSummary(dto.getAiSummary());
        report.setRiskAnalysis(dto.getRiskAnalysis());
        report.setRiskFactors(mapRiskFactors(dto.getRiskFactors()));
        report.setRecommendations(dto.getRecommendations());
        report.setFilePath(dto.getFilePath());
        report.setFileSizeBytes(dto.getFileSizeBytes());
        report.setGeneratedBy(dto.getGeneratedBy());
        report.setGeneratedDate(dto.getGeneratedDate());
        report.setGenerationTimeMs(dto.getGenerationTimeMs());
        report.setModelVersion(dto.getModelVersion());
        report.setLanguage(dto.getLanguage());

        return report;
    }

    private List<RiskFactor> mapRiskFactors(List<CreateReportDTO.RiskFactorDTO> riskFactors) {
        if (riskFactors == null) {
            return List.of();
        }
        return riskFactors.stream()
                .map(factor -> new RiskFactor(
                        factor.getFactor(),
                        parseSeverity(factor.getSeverity()),
                        factor.getDescription()))
                .toList();
    }

    private ReportType parseReportType(String reportType) {
        try {
            return ReportType.valueOf(reportType);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid report type: " + reportType);
        }
    }

    private Severity parseSeverity(String severity) {
        try {
            return Severity.valueOf(severity);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid risk factor severity: " + severity);
        }
    }
}
