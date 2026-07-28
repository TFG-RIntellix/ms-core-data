package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFactor;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ReportType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Severity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.ReportEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.RiskFactorEntity;

/**
 * Mapper class to convert between ReportEntity (infrastructure) and Report
 * (domain). Identifiers are stored as ObjectId in MongoDB and exposed as their
 * hex string representation in the domain model; enums are persisted using
 * their name.
 *
 * @author Lucía Fernández Mancebo
 * @date 29/03/2026
 */
@Component
public class ReportMapper {

    /**
     * Converts a Report domain entity into a ReportEntity for MongoDB.
     *
     * @param report the Report domain entity
     * @return the ReportEntity, or null if the input is null
     */
    public ReportEntity toEntity(Report report) {
        if (report == null) {
            return null;
        }

        ReportEntity entity = new ReportEntity();
        entity.setId(report.getId() != null ? new ObjectId(report.getId()) : null);
        entity.setPartyId(report.getPartyId() != null ? new ObjectId(report.getPartyId()) : null);
        entity.setRequestId(report.getRequestId() != null ? new ObjectId(report.getRequestId()) : null);
        entity.setScoringId(report.getScoringId() != null ? new ObjectId(report.getScoringId()) : null);
        entity.setReportType(report.getReportType() != null ? report.getReportType().name() : null);
        entity.setTitle(report.getTitle());
        entity.setAiSummary(report.getAiSummary());
        entity.setRiskAnalysis(report.getRiskAnalysis());
        entity.setRiskFactors(toRiskFactorEntities(report.getRiskFactors()));
        entity.setRecommendations(report.getRecommendations());
        entity.setFilePath(report.getFilePath());
        entity.setFileSizeBytes(report.getFileSizeBytes());
        entity.setGeneratedBy(report.getGeneratedBy());
        entity.setGeneratedDate(report.getGeneratedDate());
        entity.setGenerationTimeMs(report.getGenerationTimeMs());
        entity.setModelVersion(report.getModelVersion());
        entity.setLanguage(report.getLanguage());

        return entity;
    }

    /**
     * Converts a ReportEntity (infrastructure) into a Report domain entity.
     *
     * @param entity the ReportEntity from MongoDB
     * @return the Report domain entity, or null if the input is null
     */
    public Report toDomain(ReportEntity entity) {
        if (entity == null) {
            return null;
        }

        Report report = new Report();
        report.setId(entity.getId() != null ? entity.getId().toHexString() : null);
        report.setPartyId(entity.getPartyId() != null ? entity.getPartyId().toHexString() : null);
        report.setRequestId(entity.getRequestId() != null ? entity.getRequestId().toHexString() : null);
        report.setScoringId(entity.getScoringId() != null ? entity.getScoringId().toHexString() : null);
        report.setReportType(entity.getReportType() != null ? ReportType.valueOf(entity.getReportType()) : null);
        report.setTitle(entity.getTitle());
        report.setAiSummary(entity.getAiSummary());
        report.setRiskAnalysis(entity.getRiskAnalysis());
        report.setRiskFactors(toRiskFactorDomain(entity.getRiskFactors()));
        report.setRecommendations(entity.getRecommendations());
        report.setFilePath(entity.getFilePath());
        report.setFileSizeBytes(entity.getFileSizeBytes());
        report.setGeneratedBy(entity.getGeneratedBy());
        report.setGeneratedDate(entity.getGeneratedDate());
        report.setGenerationTimeMs(entity.getGenerationTimeMs());
        report.setModelVersion(entity.getModelVersion());
        report.setLanguage(entity.getLanguage());

        return report;
    }

    private List<RiskFactorEntity> toRiskFactorEntities(List<RiskFactor> riskFactors) {
        if (riskFactors == null) {
            return null;
        }
        return riskFactors.stream()
                .map(factor -> {
                    RiskFactorEntity entity = new RiskFactorEntity();
                    entity.setFactor(factor.getFactor());
                    entity.setSeverity(factor.getSeverity() != null ? factor.getSeverity().name() : null);
                    entity.setDescription(factor.getDescription());
                    return entity;
                })
                .toList();
    }

    private List<RiskFactor> toRiskFactorDomain(List<RiskFactorEntity> riskFactors) {
        if (riskFactors == null) {
            return null;
        }
        return riskFactors.stream()
                .map(entity -> new RiskFactor(
                        entity.getFactor(),
                        entity.getSeverity() != null ? Severity.valueOf(entity.getSeverity()) : null,
                        entity.getDescription()))
                .toList();
    }
}
