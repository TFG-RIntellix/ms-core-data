package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

/**
 * Class representing the risk metrics calculated by the scoring model.
 * Contains the standard Basel II/III metrics: Probability of Default, Loss Given Default, Exposure
 * at Default, and Expected Loss, as well as a derived risk level classification.
 * This class is used as part of the Scoring results to encapsulate all risk-related outputs in a single object.
 * @author: Lucía Fernández Mancebo
 * Date: 03-02-2026
 */
public class RiskMetrics {

    private Double probabilityOfDefault;
    private Double lossGivenDefault;
    private Double exposureAtDefault;
    private Double expectedCalculatedLoss;
    private String riskLevel;

    /**
     * Default constructor for RiskMetrics. Initializes all fields to null.
     */
    public RiskMetrics() {
    }

    /**
     * Parameterized constructor for RiskMetrics. Allows setting all fields at once.
     * @param probabilityOfDefault The probability that the borrower will default on the contract, expressed as a percentage (0-100).
     * @param lossGivenDefault  The percentage of the exposure that would be lost if a default occurs (0-100).
     * @param exposureAtDefault The total value at risk at the time of default, typically the outstanding balance of the contract.
     * @param expectedCalculatedLoss The expected loss calculated as (Probability of Default * Loss Given Default * Exposure at Default).
     * @param riskLevel A categorical classification of risk (e.g., "Low", "Medium", "High") derived from the calculated metrics, used for easier interpretation by end-users.
     */
    public RiskMetrics(Double probabilityOfDefault, Double lossGivenDefault, Double exposureAtDefault,
            Double expectedCalculatedLoss, String riskLevel) {
        this.probabilityOfDefault = probabilityOfDefault;
        this.lossGivenDefault = lossGivenDefault;
        this.exposureAtDefault = exposureAtDefault;
        this.expectedCalculatedLoss = expectedCalculatedLoss;
        this.riskLevel = riskLevel;
    }

    // Getters and Setters
    
    public Double getProbabilityOfDefault() {
        return probabilityOfDefault;
    }
    public void setProbabilityOfDefault(Double probabilityOfDefault) {
        this.probabilityOfDefault = probabilityOfDefault;
    }
    public Double getLossGivenDefault() {
        return lossGivenDefault;
    }
    public void setLossGivenDefault(Double lossGivenDefault) {
        this.lossGivenDefault = lossGivenDefault;
    }
    public Double getExposureAtDefault() {
        return exposureAtDefault;
    }
    public void setExposureAtDefault(Double exposureAtDefault) {
        this.exposureAtDefault = exposureAtDefault;
    }
    public Double getExpectedCalculatedLoss() {
        return expectedCalculatedLoss;
    }
    public void setExpectedCalculatedLoss(Double expectedCalculatedLoss) {
        this.expectedCalculatedLoss = expectedCalculatedLoss;
    }
    public String getRiskLevel() {
        return riskLevel;
    }
    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    } 

}
