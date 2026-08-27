package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

/**
 * Embedded entity mapping each element of the "xai.top_features" array
 * from the "scorings" MongoDB collection.
 * Represents a single SHAP feature contribution.
 *
 * @author Lucía Fernández Mancebo
 * @date 03/03/2026
 */
public class TopFeatureEntity {
    private String featureName;
    private String featureValue;
    private Double shapValue;
    private String description;

    public TopFeatureEntity() {
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
