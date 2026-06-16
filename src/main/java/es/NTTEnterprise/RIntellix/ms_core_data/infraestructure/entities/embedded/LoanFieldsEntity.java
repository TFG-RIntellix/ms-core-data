package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded entity mapping the "loan_fields" sub-document from the "scorings" MongoDB
 * collection.
 * Contains specific fields for loan products.
 *
 * @author Lucía Fernández Mancebo
 */
public class LoanFieldsEntity {

    @Field("requested_amount")
    private Double requestedAmount;

    @Field("term_months")
    private Integer termMonths;

    @Field("previous_loans_count")
    private Integer previousLoansCount;

    @Field("ltv")
    private Double ltv;

    public LoanFieldsEntity() {
    }

    // Getters and Setters

    public Double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public Integer getPreviousLoansCount() {
        return previousLoansCount;
    }

    public void setPreviousLoansCount(Integer previousLoansCount) {
        this.previousLoansCount = previousLoansCount;
    }

    public Double getLtv() {
        return ltv;
    }

    public void setLtv(Double ltv) {
        this.ltv = ltv;
    }
}
