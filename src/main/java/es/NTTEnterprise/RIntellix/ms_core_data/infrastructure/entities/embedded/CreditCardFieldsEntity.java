package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

/**
 * Embedded entity mapping the "credit_card_fields" sub-document from the "scorings" MongoDB
 * collection.
 * Contains specific fields for credit card products.
 *
 * @author Lucía Fernández Mancebo
 * @date 27/08/2026
 */
public class CreditCardFieldsEntity {
    private Double requestedLimit;
    private Boolean isRevolving;
    private Integer previousCardsCount;
    private Double revolvingUtilizationRate;

    public CreditCardFieldsEntity() {
    }

    // Getters and Setters

    public Double getRequestedLimit() {
        return requestedLimit;
    }

    public void setRequestedLimit(Double requestedLimit) {
        this.requestedLimit = requestedLimit;
    }

    public Boolean getIsRevolving() {
        return isRevolving;
    }

    public void setIsRevolving(Boolean isRevolving) {
        this.isRevolving = isRevolving;
    }

    public Integer getPreviousCardsCount() {
        return previousCardsCount;
    }

    public void setPreviousCardsCount(Integer previousCardsCount) {
        this.previousCardsCount = previousCardsCount;
    }

    public Double getRevolvingUtilizationRate() {
        return revolvingUtilizationRate;
    }

    public void setRevolvingUtilizationRate(Double revolvingUtilizationRate) {
        this.revolvingUtilizationRate = revolvingUtilizationRate;
    }
}
