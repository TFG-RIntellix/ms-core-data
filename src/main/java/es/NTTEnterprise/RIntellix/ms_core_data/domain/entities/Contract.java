package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

/**
 * Entity representing a Contract.
 * Placeholder for active contracts associated with a Person.
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public class Contract {

    private String id;
    private Money outstandingBalance;
    private Money monthlyPayment;

    public Contract() {
    }

    public Contract(String id, Money outstandingBalance, Money monthlyPayment) {
        this.id = id;
        this.outstandingBalance = outstandingBalance;
        this.monthlyPayment = monthlyPayment;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Money getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(Money outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public Money getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(Money monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }
}
