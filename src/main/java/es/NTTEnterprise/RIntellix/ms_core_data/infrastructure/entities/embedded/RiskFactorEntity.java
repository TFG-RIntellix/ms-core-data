package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded entity mapping each element of the "risk_factors" array from the
 * "reports" MongoDB collection. Represents a single risk factor identified by
 * the AI analyst.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-29-2026
 */
public class RiskFactorEntity {

    @Field("factor")
    private String factor;

    @Field("severity")
    private String severity;

    @Field("description")
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
