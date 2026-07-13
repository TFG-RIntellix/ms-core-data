package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output;

import java.util.Date;
import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFactor;

/**
 * Output DTO for Report.
 * Contains the report data to be returned to the client.
 */
public class ReportDTO {

    private String reportId;
    private String partyId;
    private String requestId;
    private String scoringId;
    private String reportType;
    private String title;
    private String aiSummary;
    private String riskAnalysis;
    private List<RiskFactor> riskFactors;
    private List<String> recommendations;
    private String filePath;
    private Integer fileSizeBytes;
    private String generatedBy;
    private Date generatedDate;
    private Integer generationTimeMs;
    private String modelVersion;
    private String language;

    public ReportDTO() {
    }

    // Getters and Setters

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getScoringId() {
        return scoringId;
    }

    public void setScoringId(String scoringId) {
        this.scoringId = scoringId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAiSummary() {
        return aiSummary;
    }

    public void setAiSummary(String aiSummary) {
        this.aiSummary = aiSummary;
    }

    public String getRiskAnalysis() {
        return riskAnalysis;
    }

    public void setRiskAnalysis(String riskAnalysis) {
        this.riskAnalysis = riskAnalysis;
    }

    public List<RiskFactor> getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(List<RiskFactor> riskFactors) {
        this.riskFactors = riskFactors;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Integer fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public Date getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(Date generatedDate) {
        this.generatedDate = generatedDate;
    }

    public Integer getGenerationTimeMs() {
        return generationTimeMs;
    }

    public void setGenerationTimeMs(Integer generationTimeMs) {
        this.generationTimeMs = generationTimeMs;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
