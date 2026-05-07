package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing the complete Kafka scoring consumer message.
 * Mirrors the incoming JSON schema from the scoring engine microservice.
 * Used for deserialization of Kafka messages on the PersistScoring topic.
 * Validation constraints are applied directly on fields to ensure data
 * integrity
 * at deserialization time.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
public class ScoringResultMessageDTO {

    @NotNull(message = "Request ID is required")
    @NotBlank(message = "Request ID cannot be blank")
    private String requestId;

    @NotNull(message = "Model version is required")
    @NotBlank(message = "Model version cannot be blank")
    private String modelVersion;

    @JsonProperty("executionDate")
    @NotNull(message = "Scoring date is required")
    private Date scoringDate;

    @JsonProperty("inputSnapshot")
    @NotNull(message = "Input features are required")
    @Valid
    private InputFeaturesDTO inputFeatures;

    @NotNull(message = "Risk results are required")
    @Valid
    private RiskResultsDTO results;

    @JsonProperty("explainability")
    @Valid
    private List<XAIFeatureDTO> explainability;

    @JsonProperty("baseValue")
    private Double baseValue;

    /**
     * Default constructor for ScoringResultMessageDTO.
     */
    public ScoringResultMessageDTO() {
    }

    // Getters and Setters

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public Date getScoringDate() {
        return scoringDate;
    }

    public void setScoringDate(Date scoringDate) {
        this.scoringDate = scoringDate;
    }

    public InputFeaturesDTO getInputFeatures() {
        return inputFeatures;
    }

    public void setInputFeatures(InputFeaturesDTO inputFeatures) {
        this.inputFeatures = inputFeatures;
    }

    public RiskResultsDTO getResults() {
        return results;
    }

    public void setResults(RiskResultsDTO results) {
        this.results = results;
    }

    public List<XAIFeatureDTO> getExplainability() {
        return explainability;
    }

    public void setExplainability(List<XAIFeatureDTO> explainability) {
        this.explainability = explainability;
    }

    public Double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(Double baseValue) {
        this.baseValue = baseValue;
    }
}
