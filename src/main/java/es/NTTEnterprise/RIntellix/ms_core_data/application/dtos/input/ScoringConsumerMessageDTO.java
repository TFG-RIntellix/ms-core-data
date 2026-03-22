package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import java.util.Date;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing the complete Kafka scoring consumer message.
 * Mirrors the incoming JSON schema from the scoring engine microservice.
 * Used for deserialization of Kafka messages on the PersistScoring topic.
 * Validation constraints are applied directly on fields to ensure data integrity
 * at deserialization time.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
public class ScoringConsumerMessageDTO {

    @NotNull(message = "Request ID is required")
    @NotBlank(message = "Request ID cannot be blank")
    private String requestId;

    @NotNull(message = "Model version is required")
    @NotBlank(message = "Model version cannot be blank")
    private String modelVersion;

    @NotNull(message = "Scoring date is required")
    private Date scoringDate;

    @NotNull(message = "Input features are required")
    @Valid
    private InputFeaturesDTO inputFeatures;

    @NotNull(message = "Risk results are required")
    @Valid
    private RiskResultsDTO results;

    @NotNull(message = "XAI explanation is required")
    @Valid
    private XAIExplanationDTO xai;

    /**
     * Default constructor for ScoringConsumerMessageDTO.
     */
    public ScoringConsumerMessageDTO() {
    }

    /**
     * Parameterized constructor for ScoringConsumerMessageDTO.
     * 
     * @param requestId     reference to the evaluated request
     * @param modelVersion  version of the model used
     * @param scoringDate   date and time of scoring calculation
     * @param inputFeatures snapshot of input features used by the model
     * @param results       risk metrics computed by the model
     * @param xai           explainability information with SHAP values
     */
    public ScoringConsumerMessageDTO(String requestId, String modelVersion, Date scoringDate,
            InputFeaturesDTO inputFeatures, RiskResultsDTO results, XAIExplanationDTO xai) {
        this.requestId = requestId;
        this.modelVersion = modelVersion;
        this.scoringDate = scoringDate;
        this.inputFeatures = inputFeatures;
        this.results = results;
        this.xai = xai;
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

    public XAIExplanationDTO getXai() {
        return xai;
    }

    public void setXai(XAIExplanationDTO xai) {
        this.xai = xai;
    }
}
