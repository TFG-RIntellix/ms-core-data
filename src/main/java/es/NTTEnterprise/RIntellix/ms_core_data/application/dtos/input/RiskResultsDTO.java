package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import es.NTTEnterprise.RIntellix.ms_core_data.application.constraints.NonNegativeAmount;
import es.NTTEnterprise.RIntellix.ms_core_data.application.constraints.ValidPercentage;
import es.NTTEnterprise.RIntellix.ms_core_data.application.constraints.ValidRiskGrade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing the risk results section of a Kafka scoring message.
 * Contains the calculated risk metrics: PD, LGD, EAD, ECL, and risk grade.
 * Validation constraints are applied directly on fields to ensure data integrity.
 * Part of the ScoringConsumerMessageDTO nested structure.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
public class RiskResultsDTO {

    @NotNull(message = "Probability of Default is required")
    @ValidPercentage(message = "Probability of Default must be between 0 and 1")
    private Double pd;

    @NotNull(message = "Loss Given Default is required")
    @ValidPercentage(message = "Loss Given Default must be between 0 and 1")
    private Double lgd;

    @NotNull(message = "Exposure at Default is required")
    @NonNegativeAmount(message = "Exposure at Default cannot be negative")
    private Double ead;

    @NotNull(message = "Expected Credit Loss is required")
    @NonNegativeAmount(message = "Expected Credit Loss cannot be negative")
    private Double ecl;

    @NotNull(message = "Risk grade is required")
    @NotBlank(message = "Risk grade cannot be blank")
    @ValidRiskGrade(message = "Risk grade must be one of the valid values (A, B, C, D, E, F)")
    private String riskGrade;

    /**
     * Default constructor for RiskResultsDTO.
     */
    public RiskResultsDTO() {
    }

    /**
     * Parameterized constructor for RiskResultsDTO.
     * 
     * @param pd        Probability of Default (0-1)
     * @param lgd       Loss Given Default (0-1)
     * @param ead       Exposure at Default
     * @param ecl       Expected Credit Loss
     * @param riskGrade Risk grade classification (A-F)
     */
    public RiskResultsDTO(Double pd, Double lgd, Double ead, Double ecl, String riskGrade) {
        this.pd = pd;
        this.lgd = lgd;
        this.ead = ead;
        this.ecl = ecl;
        this.riskGrade = riskGrade;
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
