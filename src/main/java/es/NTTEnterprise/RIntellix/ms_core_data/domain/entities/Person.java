package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.util.ArrayList;
import java.util.List;


/**
 * Entity representing a Person (individual party).
 * Extends Party and contains identity, demographic, financial and contact information.
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public class Person {

    // Identity
    private String firstName;
    private String lastName;
    private String nif;

    // Grouped Data
    private SocioDemographicProfile demographics;
    private FinancialProfile financials;
    private ContactInfo contactInfo;
    private List<Contract> activeContracts;

    public Person() {
        this.activeContracts = new ArrayList<>();
    }

    public Person(String firstName, String lastName, String nif) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nif = nif;
        this.activeContracts = new ArrayList<>();
    }

    /**
     * Gets the full name of the person.
     * @return firstName + lastName
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Calculates the age based on demographics profile.
     * @return the age in years, or null if demographics is not set
     */
    public Integer getAge() {
        if (demographics == null) {
            return null;
        }
        return demographics.getAge();
    }

    /**
     * Adds a contract to the list of active contracts.
     * @param contract the contract to add
     */
    public void addContract(Contract contract) {
        if (this.activeContracts == null) {
            this.activeContracts = new ArrayList<>();
        }
        this.activeContracts.add(contract);
    }

    /**
     * Calculates the total outstanding debt from all active contracts.
     * If there are no active contracts, returns a Money object with zero amount.
     * 
     * @return the total debt as a Money object
     */
    public Money getTotalDebt() {
        if (activeContracts == null || activeContracts.isEmpty()) {
            return new Money(0.0, "EUR");
        }

        Money totalDebt = new Money(0.0, "EUR");
        for (Contract contract : activeContracts) {
            if (contract.getOutstandingBalance() != null) {
                totalDebt = totalDebt.add(contract.getOutstandingBalance());
            }
        }
        return totalDebt;
    }
    /**
     * Method to calculate the global Debt-To-Income ratio for the person.
     * The form is the following: DTI = Total Debt / Annual Income.
     * If the annual income is zero or not available, returns null to indicate that DTI cannot be calculated.
     * @return the DTI ratio as a Double, or null if it cannot be calculated
     */
    public Double getGlobalDTI() {
        if (financials == null || financials.getAnnualIncome() == null 
            || financials.getAnnualIncome().getAmount() == null 
            || financials.getAnnualIncome().getAmount() == 0) {
            return null;
        }

        Money totalDebt = getTotalDebt();
        if (totalDebt == null || totalDebt.getAmount() == null) {
            return 0.0;
        }

        return totalDebt.getAmount() / financials.getAnnualIncome().getAmount();
    }

    // Getters and Setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public SocioDemographicProfile getDemographics() {
        return demographics;
    }

    public void setDemographics(SocioDemographicProfile demographics) {
        this.demographics = demographics;
    }

    public FinancialProfile getFinancials() {
        return financials;
    }

    public void setFinancials(FinancialProfile financials) {
        this.financials = financials;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<Contract> getActiveContracts() {
        return activeContracts;
    }

    public void setActiveContracts(List<Contract> activeContracts) {
        this.activeContracts = activeContracts;
    }
}
