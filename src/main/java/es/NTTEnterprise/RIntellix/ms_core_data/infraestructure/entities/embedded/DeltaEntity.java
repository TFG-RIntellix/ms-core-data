package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded entity mapping the "delta" sub-document from the "simulations"
 * MongoDB collection.
 * Contains the computed differences between the base scoring and the simulated
 * scenario.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public class DeltaEntity {

    @Field("pd_change")
    private Double pdChange;

    @Field("el_change")
    private Double elChange;

    @Field("risk_grade_change")
    private String riskGradeChange;

    public DeltaEntity() {
    }

    // Getters and Setters

    public Double getPdChange() {
        return pdChange;
    }

    public void setPdChange(Double pdChange) {
        this.pdChange = pdChange;
    }

    public Double getElChange() {
        return elChange;
    }

    public void setElChange(Double elChange) {
        this.elChange = elChange;
    }

    public String getRiskGradeChange() {
        return riskGradeChange;
    }

    public void setRiskGradeChange(String riskGradeChange) {
        this.riskGradeChange = riskGradeChange;
    }
}
