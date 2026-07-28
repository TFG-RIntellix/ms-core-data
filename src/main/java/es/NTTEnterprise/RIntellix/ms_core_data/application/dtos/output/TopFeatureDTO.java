package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output;

/**
 * Data Transfer Object (DTO) for a top contributing feature in the SHAP
 * explainability.
 * Represents a single feature with its name, value at calculation time, and
 * SHAP contribution.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public class TopFeatureDTO {

    private String featureName;
    private String featureValue;
    private Double shapValue;
    private String description;

    public TopFeatureDTO() {
    }

    public TopFeatureDTO(String featureName, String featureValue, Double shapValue, String description) {
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
