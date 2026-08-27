package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

/**
 * Embedded document for party economic data.
 * Contains income and financial information.
 * 
 * @author Lucía Fernández Mancebo
 * @date 01/03/2026
 */
public class EconomicDataEntity {
    private Double annualIncome;
    private String incomeType;
    private String currency;
    private Double existingObligations;
    private Boolean hasMortgage;

    public EconomicDataEntity() {
    }

    // Getters and Setters

    public Double getExistingObligations() {
        return existingObligations;
    }

    public void setExistingObligations(Double existingObligations) {
        this.existingObligations = existingObligations;
    }

    public Boolean getHasMortgage() {
        return hasMortgage;
    }

    public void setHasMortgage(Boolean hasMortgage) {
        this.hasMortgage = hasMortgage;
    }

    // Getters and Setters

    public Double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
