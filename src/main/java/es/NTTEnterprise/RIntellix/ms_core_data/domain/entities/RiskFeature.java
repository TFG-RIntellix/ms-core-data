package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

/**
 * Class representing the individual features that contribute to the risk assessment in the scoring model.
 * Each RiskFeature encapsulates the name of the feature, its SHAP value (which quantifies its contribution to the final risk score),
 * and a human-readable description for explainability purposes. 
 * This class is used in the Scoring results to provide detailed insights into which features are driving the risk assessment for a given contract.
 * @author Lucía Fernández Mancebo
 * Date: 03-02-2026
 */
public class RiskFeature {

    private String featureName;
    private String featureValue;
    private Double shapValue;
    private String description;

    /**
     * Default constructor for RiskFeature. Initializes all fields to null.
     */
    public RiskFeature() {
    }

    /**
     * Parameterized constructor for RiskFeature. Allows setting all fields at once.
     * @param featureName The name of the feature (e.g., "Income", "Credit Score") that contributed to the risk assessment.
     * @param featureValue The value of the feature at the time of calculation, stored as String for uniformity.
     * @param shapValue The SHAP value representing the contribution of this feature to the final risk score. Positive values indicate increased risk, while negative values indicate decreased risk.
     * @param description A human-readable description of the feature and its impact on the risk score, used for explainability purposes in the scoring results.
     */
    public RiskFeature(String featureName, String featureValue, Double shapValue, String description) {
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
