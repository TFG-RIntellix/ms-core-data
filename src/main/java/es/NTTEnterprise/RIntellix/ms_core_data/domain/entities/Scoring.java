package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.util.Date;
import java.util.List;

/**
 * Domain entity representing the credit risk scoring result calculated for a request.
 * Includes model metadata, input features snapshot, risk metrics and SHAP explainability.
 * @author Lucía Fernández Mancebo
 * Date: 03-02-2026
 */
public class Scoring {

    private String id;
    private String requestId;
    private String modelVersion;
    private Date executionDate;
    private ModelInputs inputSnapshot;
    private RiskMetrics results;
    private Double baseValue;
    private List<RiskFeature> explainability;

    /**
     * Default constructor for Scoring.
     */
    public Scoring() {
    }

    /**
     * Parameterized constructor for Scoring.
     * @param id              Unique identifier.
     * @param requestId       Reference to the evaluated request.
     * @param modelVersion    Version of the model used (e.g. xgboost_pd_v1).
     * @param executionDate   Date and time when the scoring was computed.
     * @param inputSnapshot   Snapshot of the input features used by the model.
     * @param results         Risk metrics computed by the model (PD, LGD, EAD, ECL, risk grade).
     * @param baseValue       SHAP base value (expected value) of the model.
     * @param explainability  Top contributing features with their SHAP values.
     */
    public Scoring(String id, String requestId, String modelVersion, Date executionDate,
                   ModelInputs inputSnapshot, RiskMetrics results, Double baseValue,
                   List<RiskFeature> explainability) {
        this.id = id;
        this.requestId = requestId;
        this.modelVersion = modelVersion;
        this.executionDate = executionDate;
        this.inputSnapshot = inputSnapshot;
        this.results = results;
        this.baseValue = baseValue;
        this.explainability = explainability;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public ModelInputs getInputSnapshot() {
        return inputSnapshot;
    }

    public void setInputSnapshot(ModelInputs inputSnapshot) {
        this.inputSnapshot = inputSnapshot;
    }

    public RiskMetrics getResults() {
        return results;
    }

    public void setResults(RiskMetrics results) {
        this.results = results;
    }

    public Double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(Double baseValue) {
        this.baseValue = baseValue;
    }

    public List<RiskFeature> getExplainability() {
        return explainability;
    }

    public void setExplainability(List<RiskFeature> explainability) {
        this.explainability = explainability;
    }

}
