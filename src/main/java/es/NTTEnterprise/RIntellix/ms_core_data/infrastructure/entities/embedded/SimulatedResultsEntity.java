package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

/**
 * Embedded entity mapping the "simulated_results" sub-document from the
 * "simulations" MongoDB collection.
 * Contains the risk metrics computed by the model with the simulation's
 * modified inputs applied.
 *
 * @author Lucía Fernández Mancebo
 * @date 03/03/2026
 */
public class SimulatedResultsEntity {
    private Double pd;
    private Double lgd;
    private Double ead;
    private Double ecl;
    private String riskGrade;
    private String decision;
    private FinancialMetricsEntity financialMetrics;

    public SimulatedResultsEntity() {
    }

    // Getters and Setters

    public Double getPd() {
        return pd;
    }

    public void setPd(Double pd) {
        this.pd = pd;
    }

    public Double getLgd() {
        return lgd;
    }

    public void setLgd(Double lgd) {
        this.lgd = lgd;
    }

    public Double getEad() {
        return ead;
    }

    public void setEad(Double ead) {
        this.ead = ead;
    }

    public Double getEcl() {
        return ecl;
    }

    public void setEcl(Double ecl) {
        this.ecl = ecl;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public FinancialMetricsEntity getFinancialMetrics() {
        return financialMetrics;
    }

    public void setFinancialMetrics(FinancialMetricsEntity financialMetrics) {
        this.financialMetrics = financialMetrics;
    }
}
