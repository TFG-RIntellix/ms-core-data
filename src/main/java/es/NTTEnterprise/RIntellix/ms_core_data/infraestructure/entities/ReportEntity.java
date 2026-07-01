package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;

import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.RiskFactorEntity;

/**
 * Infrastructure entity mapping the "reports" MongoDB collection.
 * Represents an AI-generated risk report produced by ms-reporting for a given
 * scoring, including the natural-language analysis, identified risk factors,
 * recommendations and the generated PDF metadata.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-29-2026
 */
@Document(collection = "reports")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportEntity {

    @Id
    private ObjectId id;

    @Field("party_id")
    private ObjectId partyId;

    @Field("request_id")
    private ObjectId requestId;

    @Field("scoring_id")
    private ObjectId scoringId;

    @Field("report_type")
    private String reportType;

    @Field("title")
    private String title;

    @Field("ai_summary")
    private String aiSummary;

    @Field("risk_analysis")
    private String riskAnalysis;

    @Field("risk_factors")
    private List<RiskFactorEntity> riskFactors;

    @Field("recommendations")
    private List<String> recommendations;

    @Field("file_path")
    private String filePath;

    @Field("file_size_bytes")
    private Integer fileSizeBytes;

    @Field("generated_by")
    private String generatedBy;

    @Field("generated_date")
    private Date generatedDate;

    @Field("generation_time_ms")
    private Integer generationTimeMs;

    @Field("model_version")
    private String modelVersion;

    @Field("language")
    private String language;

    public ReportEntity() {
    }

    // Getters and Setters

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getPartyId() {
        return partyId;
    }

    public void setPartyId(ObjectId partyId) {
        this.partyId = partyId;
    }

    public ObjectId getRequestId() {
        return requestId;
    }

    public void setRequestId(ObjectId requestId) {
        this.requestId = requestId;
    }

    public ObjectId getScoringId() {
        return scoringId;
    }

    public void setScoringId(ObjectId scoringId) {
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

    public List<RiskFactorEntity> getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(List<RiskFactorEntity> riskFactors) {
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
