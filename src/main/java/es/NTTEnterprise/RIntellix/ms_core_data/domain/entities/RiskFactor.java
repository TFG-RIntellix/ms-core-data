package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Severity;

/**
 * Domain entity representing a single risk factor identified by the AI analyst
 * within a generated report. Each factor is classified by severity and
 * described in natural language.
 *
 * @author Lucía Fernández Mancebo
 * @date 29/03/2026
 */
public class RiskFactor {

    private String factor;
    private Severity severity;
    private String description;

    public RiskFactor() {
    }

    public RiskFactor(String factor, Severity severity, String description) {
        this.factor = factor;
        this.severity = severity;
        this.description = description;
    }

    // Getters and Setters

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RiskFactor [factor=" + factor + ", severity=" + severity + ", description=" + description + "]";
    }
}
