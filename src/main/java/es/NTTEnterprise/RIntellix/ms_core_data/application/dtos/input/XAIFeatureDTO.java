package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

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

    private String featureName;

    private String featureValue;

    private Double shapValue;

    private String description;

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
     * @param description  description of the feature contribution
     */
    public XAIFeatureDTO(String featureName, String featureValue, Double shapValue, String description) {
        this.featureName = featureName;
        this.featureValue = featureValue;
        this.shapValue = shapValue;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
