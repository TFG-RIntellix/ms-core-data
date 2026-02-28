package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;
/**
 * This class represents a monetary amount with its currency. It contains the amount as a double and the currency as a string. 
 * It also has a method to add two Money objects together, but only if they have the same currency.
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
public class Money {

    private Double amount;
    private String currency;

    /**
     * Constructor for Money class. 
     * @param amount the amount of money as a double
     * @param currency the currency of the money as a string
     */
    public Money(Double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * This method adds two Money objects together, but only if they have the same currency. If they have different currencies, it throws an IllegalArgumentException.
     * @param other the other Money object to be added to this one
     * @return a new Money object that is the sum of this and the other Money object, if they have the same currency
     */
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add Money with different currencies");
        }
        return new Money(this.amount + other.amount, this.currency);
    }

    // Getters and setters for amount and currency

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return amount.hashCode() + currency.hashCode();
    }
}
