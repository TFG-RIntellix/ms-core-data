package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document for party economic data.
 * Contains income and financial information.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public class EconomicDataEntity {

    @Field("annual_income")
    private Double annualIncome;

    @Field("income_type")
    private String incomeType;

    @Field("currency")
    private String currency;

    @Field("existing_obligations")
    private Double existingObligations;

    @Field("has_mortage")
    private Boolean hasMortage;

    public EconomicDataEntity() {
    }

    // Getters and Setters

    public Double getExistingObligations() {
        return existingObligations;
    }

    public void setExistingObligations(Double existingObligations) {
        this.existingObligations = existingObligations;
    }

    public Boolean getHasMortage() {
        return hasMortage;
    }

    public void setHasMortage(Boolean hasMortage) {
        this.hasMortage = hasMortage;
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
