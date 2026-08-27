package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

/**
 * Embedded entity mapping each element of the "risk_factors" array from the
 * "reports" MongoDB collection. Represents a single risk factor identified by
 * the AI analyst.
 *
 * @author Lucía Fernández Mancebo
 * @date 29/03/2026
 */
public class RiskFactorEntity {
    private String factor;
    private String severity;
    private String description;

    public RiskFactorEntity() {
    }

    // Getters and Setters

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
