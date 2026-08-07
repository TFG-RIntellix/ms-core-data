package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.NTTEnterprise.RIntellix.ms_core_data.application.constraints.NonNegativeAmount;
import es.NTTEnterprise.RIntellix.ms_core_data.application.constraints.ValidPercentage;
import es.NTTEnterprise.RIntellix.ms_core_data.application.constraints.ValidRiskGrade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO representing the risk results section of a Kafka scoring message.
 * Contains the calculated risk metrics: PD, LGD, EAD, ECL, and risk grade,
 * as well as financial affordability metrics (new as of 05-26-2026).
 * Validation constraints are applied directly on fields to ensure data
 * integrity.
 * Part of the ScoringConsumerMessageDTO nested structure.
 * 
 * @author Lucía Fernández Mancebo
 * @date 21/03/2026
 * @Updated 05-26-2026 - Added financialMetrics field
 */
public class RiskResultsDTO {

    @JsonProperty("probabilityOfDefault")
    private String pd; // Accept as String since Kafka message sends string values

    @JsonProperty("lossGivenDefault")
    @ValidPercentage(message = "Loss Given Default must be between 0 and 1")
    private Double lgd;

    @JsonProperty("exposureAtDefault")
    @NonNegativeAmount(message = "Exposure at Default cannot be negative")
    private Double ead;

    @JsonProperty("expectedCalculatedLoss")
    private String ecl; // Accept as String since Kafka message sends string values

    @JsonProperty("riskLevel")
    @NotBlank(message = "Risk grade cannot be blank")
    @ValidRiskGrade(message = "Risk grade must be one of the valid values (A, B, C, D, E, F, G, H)")
    private String riskGrade;

    @JsonProperty("financialMetrics")
    @Valid
    private FinancialMetricsDTO financialMetrics;

    /**
     * Default constructor for RiskResultsDTO.
     */
    public RiskResultsDTO() {
    }

    /**
     * Parameterized constructor for RiskResultsDTO.
     * 
     * @param pd        Probability of Default as String (will be converted to
     *                  Double)
     * @param lgd       Loss Given Default (0-1)
     * @param ead       Exposure at Default
     * @param ecl       Expected Credit Loss as String (will be converted to Double)
     * @param riskGrade Risk grade classification (A-H)
     */
    public RiskResultsDTO(String pd, Double lgd, Double ead, String ecl, String riskGrade) {
        this.pd = pd;
        this.lgd = lgd;
        this.ead = ead;
        this.ecl = ecl;
        this.riskGrade = riskGrade;
    }

    // Getters and Setters

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
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

    public String getEcl() {
        return ecl;
    }

    public void setEcl(String ecl) {
        this.ecl = ecl;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }

    public FinancialMetricsDTO getFinancialMetrics() {
        return financialMetrics;
    }

    public void setFinancialMetrics(FinancialMetricsDTO financialMetrics) {
        this.financialMetrics = financialMetrics;
    }
}
