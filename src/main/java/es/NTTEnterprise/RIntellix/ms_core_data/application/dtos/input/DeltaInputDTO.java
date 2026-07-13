package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

/**
 * Nested input DTO for simulation deltas, representing differences in PD, ECL,
 * risk grade, and monthly affordability metrics.
 *
 * @author Lucía Fernández Mancebo
 * @Date 07-10-2026
 */
public class DeltaInputDTO {

    private Double pdChange;
    private Double eclChange;
    private String riskGradeChange;
    private Double monthlyPaymentChange;
    private Double dtiChange;
    private Double totalPaymentChange;
    private Double totalInterestChange;
    private Double monthlyDisposableIncomeChange;

    public DeltaInputDTO() {
    }

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
