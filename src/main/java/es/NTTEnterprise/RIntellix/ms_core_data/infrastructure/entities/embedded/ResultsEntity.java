package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

/**
 * Embedded entity mapping the "results" sub-document from the "scorings"
 * MongoDB collection.
 * Contains the standard Basel II/III risk metrics: PD, LGD, EAD, ECL and a risk
 * grade, as well as financial affordability metrics (as of 05-26-2026).
 *
 * @author Lucía Fernández Mancebo
 * @date 03/03/2026
 * @Updated 05-26-2026 - Added FinancialMetricsEntity
 */
public class ResultsEntity {
    private Double pd;
    private Double lgd;
    private Double ead;
    private Double ecl;
    private String riskGrade;
    private Double creditLimitAssigned;
    private FinancialMetricsEntity financialMetrics;

    public ResultsEntity() {
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

    public Double getCreditLimitAssigned() {
        return creditLimitAssigned;
    }

    public void setCreditLimitAssigned(Double creditLimitAssigned) {
        this.creditLimitAssigned = creditLimitAssigned;
    }

    public FinancialMetricsEntity getFinancialMetrics() {
        return financialMetrics;
    }

    public void setFinancialMetrics(FinancialMetricsEntity financialMetrics) {
        this.financialMetrics = financialMetrics;
    }
}
