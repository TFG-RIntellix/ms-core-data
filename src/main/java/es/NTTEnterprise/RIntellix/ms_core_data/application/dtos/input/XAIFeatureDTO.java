package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing a single SHAP feature contribution in the XAI explanation.
 * Validation constraints are applied directly on fields to ensure data
 * integrity.
 * Part of the ScoringConsumerMessageDTO nested structure for Kafka message
 * deserialization.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
public class XAIFeatureDTO {

    @NotNull(message = "Feature name is required")
    @NotBlank(message = "Feature name cannot be blank")
    private String featureName;

    @NotNull(message = "Feature value is required")
    @NotBlank(message = "Feature value cannot be blank")
    private String featureValue;

    @NotNull(message = "SHAP value is required")
    private Double shapValue;

    /**
     * Default constructor for XAIFeatureDTO.
     */
    public XAIFeatureDTO() {
    }

    /**
     * Parameterized constructor for XAIFeatureDTO.
     * 
     * @param featureName  the name of the feature
     * @param featureValue the value of the feature at calculation time
     * @param shapValue    the SHAP contribution of this feature to the risk score
     */
    public XAIFeatureDTO(String featureName, String featureValue, Double shapValue) {
        this.featureName = featureName;
        this.featureValue = featureValue;
        this.shapValue = shapValue;
    }

    // Getters and Setters

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }

    public Double getShapValue() {
        return shapValue;
    }

    public void setShapValue(Double shapValue) {
        this.shapValue = shapValue;
    }
}
