package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded entity mapping the "results" sub-document from the "scorings"
 * MongoDB collection.
 * Contains the standard Basel II/III risk metrics: PD, LGD, EAD, ECL and a risk
 * grade.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public class ResultsEntity {

    @Field("pd")
    private Double pd;

    @Field("lgd")
    private Double lgd;

    @Field("ead")
    private Double ead;

    @Field("ecl")
    private Double ecl;

    @Field("risk_grade")
    private String riskGrade;

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
}
