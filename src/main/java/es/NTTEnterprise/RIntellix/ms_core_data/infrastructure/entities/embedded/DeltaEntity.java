package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

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

    @Field("ecl_change")
    private Double eclChange;

    @Field("risk_grade_change")
    private String riskGradeChange;

    @Field("monthly_payment_change")
    private Double monthlyPaymentChange;

    @Field("dti_change")
    private Double dtiChange;

    @Field("total_payment_change")
    private Double totalPaymentChange;

    @Field("total_interest_change")
    private Double totalInterestChange;

    @Field("monthly_disposable_income_change")
    private Double monthlyDisposableIncomeChange;

    public DeltaEntity() {
    }

    // Getters and Setters

    public Double getPdChange() {
        return pdChange;
    }

    public void setPdChange(Double pdChange) {
        this.pdChange = pdChange;
    }

    public Double getEclChange() {
        return eclChange;
    }

    public void setEclChange(Double eclChange) {
        this.eclChange = eclChange;
    }

    public String getRiskGradeChange() {
        return riskGradeChange;
    }

    public void setRiskGradeChange(String riskGradeChange) {
        this.riskGradeChange = riskGradeChange;
    }

    public Double getMonthlyPaymentChange() {
        return monthlyPaymentChange;
    }

    public void setMonthlyPaymentChange(Double monthlyPaymentChange) {
        this.monthlyPaymentChange = monthlyPaymentChange;
    }

    public Double getDtiChange() {
        return dtiChange;
    }

    public void setDtiChange(Double dtiChange) {
        this.dtiChange = dtiChange;
    }

    public Double getTotalPaymentChange() {
        return totalPaymentChange;
    }

    public void setTotalPaymentChange(Double totalPaymentChange) {
        this.totalPaymentChange = totalPaymentChange;
    }

    public Double getTotalInterestChange() {
        return totalInterestChange;
    }

    public void setTotalInterestChange(Double totalInterestChange) {
        this.totalInterestChange = totalInterestChange;
    }

    public Double getMonthlyDisposableIncomeChange() {
        return monthlyDisposableIncomeChange;
    }

    public void setMonthlyDisposableIncomeChange(Double monthlyDisposableIncomeChange) {
        this.monthlyDisposableIncomeChange = monthlyDisposableIncomeChange;
    }
}
