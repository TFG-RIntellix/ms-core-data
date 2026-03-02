package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document for party credit history.
 * Contains information about previous loans and payment behavior.
 * This subdocument is optional and may not exist for new customers.
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public class CreditHistoryEntity {

    @Field("previous_loans_count")
    private Integer previousLoansCount;

    @Field("previous_loans_amount")
    private Double previousLoansAmount;

    @Field("previous_repayments_amount")
    private Double previousRepaymentsAmount;

    @Field("previous_defaults_count")
    private Integer previousDefaultsCount;

    @Field("is_new_customer")
    private Boolean isNewCustomer;

    public CreditHistoryEntity() {
    }

    // Getters and Setters

    public Integer getPreviousLoansCount() {
        return previousLoansCount;
    }

    public void setPreviousLoansCount(Integer previousLoansCount) {
        this.previousLoansCount = previousLoansCount;
    }

    public Double getPreviousLoansAmount() {
        return previousLoansAmount;
    }

    public void setPreviousLoansAmount(Double previousLoansAmount) {
        this.previousLoansAmount = previousLoansAmount;
    }

    public Double getPreviousRepaymentsAmount() {
        return previousRepaymentsAmount;
    }

    public void setPreviousRepaymentsAmount(Double previousRepaymentsAmount) {
        this.previousRepaymentsAmount = previousRepaymentsAmount;
    }

    public Integer getPreviousDefaultsCount() {
        return previousDefaultsCount;
    }

    public void setPreviousDefaultsCount(Integer previousDefaultsCount) {
        this.previousDefaultsCount = previousDefaultsCount;
    }

    public Boolean getIsNewCustomer() {
        return isNewCustomer;
    }

    public void setIsNewCustomer(Boolean isNewCustomer) {
        this.isNewCustomer = isNewCustomer;
    }
}
