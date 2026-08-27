package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

/**
 * Embedded entity mapping the "loan_fields" sub-document from the "scorings" MongoDB
 * collection.
 * Contains specific fields for loan products.
 *
 * @author Lucía Fernández Mancebo
 */
public class LoanFieldsEntity {
    private Double loanAmount;
    private Integer termMonths;
    private Integer previousLoansCount;
    private Double ltv;

    public LoanFieldsEntity() {
    }

    // Getters and Setters

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
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
