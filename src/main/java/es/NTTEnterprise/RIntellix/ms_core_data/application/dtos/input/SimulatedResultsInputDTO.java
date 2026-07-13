package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Nested input DTO for simulated results containing PD, LGD, EAD, ECL, risk grade,
 * decision, and detailed financial affordability metrics.
 *
 * @author Lucía Fernández Mancebo
 * @Date 07-10-2026
 */
public class SimulatedResultsInputDTO {

    @NotNull(message = "Simulated PD must be provided")
    private Double pd;

    @NotNull(message = "Simulated LGD must be provided")
    private Double lgd;

    @NotNull(message = "Simulated EAD must be provided")
    private Double ead;

    @NotNull(message = "Simulated ECL must be provided")
    private Double ecl;

    @NotBlank(message = "Simulated risk grade must be provided")
    private String riskGrade;

    @NotBlank(message = "Simulated decision must be provided")
    private String decision;

    private Double monthlyPayment;
    private Double dti;
    private Double totalPayment;
    private Double totalInterest;
    private Double disposableIncome;

    public SimulatedResultsInputDTO() {
    }

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

    public Double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(Double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public Double getDti() {
        return dti;
    }

    public void setDti(Double dti) {
        this.dti = dti;
    }

    public Double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Double getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(Double totalInterest) {
        this.totalInterest = totalInterest;
    }

    public Double getDisposableIncome() {
        return disposableIncome;
    }

    public void setDisposableIncome(Double disposableIncome) {
        this.disposableIncome = disposableIncome;
    }
}
