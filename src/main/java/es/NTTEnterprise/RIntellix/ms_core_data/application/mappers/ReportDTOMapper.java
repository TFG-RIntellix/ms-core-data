package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;

/**
 * Mapper that converts a {@link Report} domain entity into a {@link ReportDTO}.
 */
/**
 * Core component: ReportDTOMapper.
 * Encapsulates the logic and responsibilities assigned to this element
 * within the Hexagonal Architecture, ensuring separation of concerns.
 *
 * @author Lucía Fernández Mancebo
 * @date 28/07/2026
 */
public class ReportDTOMapper {

    public ReportDTO toDTO(Report report) {
        if (report == null) {
            return null;
        }

        ReportDTO dto = new ReportDTO();

        dto.setReportId(report.getId());
        dto.setPartyId(report.getPartyId());
        dto.setRequestId(report.getRequestId());
        dto.setScoringId(report.getScoringId());
        dto.setReportType(report.getReportType() != null ? report.getReportType().name() : null);
        dto.setTitle(report.getTitle());
        dto.setAiSummary(report.getAiSummary());
        dto.setRiskAnalysis(report.getRiskAnalysis());
        dto.setRiskFactors(report.getRiskFactors());
        dto.setRecommendations(report.getRecommendations());
        dto.setFilePath(report.getFilePath());
        dto.setFileSizeBytes(report.getFileSizeBytes());
        dto.setGeneratedBy(report.getGeneratedBy());
        dto.setGeneratedDate(report.getGeneratedDate());
        dto.setGenerationTimeMs(report.getGenerationTimeMs());
        dto.setModelVersion(report.getModelVersion());
        dto.setLanguage(report.getLanguage());

        return dto;
    }
}
