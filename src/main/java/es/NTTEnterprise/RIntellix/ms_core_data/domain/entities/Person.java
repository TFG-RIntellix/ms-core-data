package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.util.ArrayList;
import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Entity representing a Person (individual party).
 * Extends Party and contains identity, demographic, financial and contact
 * information.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
@Slf4j
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
     * 
     * @return firstName + lastName
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Calculates the age based on demographics profile.
     * 
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
     * 
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
     * Each contract subtype provides its outstanding debt via getOutstandingDebt().
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
            Money debt = contract.getOutstandingDebt();
            if (debt != null) {
                totalDebt = totalDebt.add(debt);
            }
        }
        log.debug(LogMessage.DOMAIN_TOTAL_DEBT_RESULT, totalDebt);
        return totalDebt;
    }

    /**
     * Calculates the sum of all monthly payment contributions from active
     * contracts.
     * Each contract subtype calculates its own monthly payment using the
     * appropriate formula.
     *
     * @return the total monthly debt payment as a Money object
     */
    public Money getTotalMonthlyDebtPayment() {
        if (activeContracts == null || activeContracts.isEmpty()) {
            return new Money(0.0, "EUR");
        }

        Money totalMonthly = new Money(0.0, "EUR");
        for (Contract contract : activeContracts) {
            Money monthly = contract.calculateMonthlyPayment();
            if (monthly != null) {
                totalMonthly = totalMonthly.add(monthly);
            }
        }
        log.debug(LogMessage.DOMAIN_TOTAL_MONTHLY_PAYMENT_RESULT, totalMonthly);
        return totalMonthly;
    }

    /**
     * Calculates the global Debt-To-Income (DTI) ratio for the person.
     * Formula: DTI = Σ(monthly payments) / Gross Monthly Income
     * Where: Gross Monthly Income = Annual Income / 12
     *
     * Each contract type contributes its monthly payment via
     * calculateMonthlyPayment():
     * - Loan/Mortgage: French amortization system (cuota constante)
     * - CreditCard Non-Revolving: currentBalance / 12
     * - CreditCard Revolving: French system over 12 months with interest
     *
     * @return the DTI ratio as a Double, or null if it cannot be calculated
     */
    public Double getGlobalDTI() {
        if (financials == null || financials.getAnnualIncome() == null
                || financials.getAnnualIncome().getAmount() == null
                || financials.getAnnualIncome().getAmount() == 0) {
            log.debug(LogMessage.DOMAIN_DTI_NO_INCOME);
            return null;
        }

        Money totalMonthlyPayment = getTotalMonthlyDebtPayment();
        if (totalMonthlyPayment == null || totalMonthlyPayment.getAmount() == null) {
            return 0.0;
        }

        Double grossMonthlyIncome = financials.getAnnualIncome().getAmount() / 12.0;
        Double dti = totalMonthlyPayment.getAmount() / grossMonthlyIncome;
        log.debug(LogMessage.DOMAIN_DTI_RESULT, totalMonthlyPayment, grossMonthlyIncome, dti);
        return dti;
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

    @Override
    public String toString() {
        return "Person [firstName=" + firstName + ", lastName=" + lastName + ", nif=" + nif + ", demographics="
                + demographics + ", financials=" + financials + ", contactInfo=" + contactInfo + ", activeContracts="
                + activeContracts + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((nif == null) ? 0 : nif.hashCode());
        result = prime * result + ((demographics == null) ? 0 : demographics.hashCode());
        result = prime * result + ((financials == null) ? 0 : financials.hashCode());
        result = prime * result + ((contactInfo == null) ? 0 : contactInfo.hashCode());
        result = prime * result + ((activeContracts == null) ? 0 : activeContracts.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (nif == null) {
            if (other.nif != null)
                return false;
        } else if (!nif.equals(other.nif))
            return false;
        if (demographics == null) {
            if (other.demographics != null)
                return false;
        } else if (!demographics.equals(other.demographics))
            return false;
        if (financials == null) {
            if (other.financials != null)
                return false;
        } else if (!financials.equals(other.financials))
            return false;
        if (contactInfo == null) {
            if (other.contactInfo != null)
                return false;
        } else if (!contactInfo.equals(other.contactInfo))
            return false;
        if (activeContracts == null) {
            if (other.activeContracts != null)
                return false;
        } else if (!activeContracts.equals(other.activeContracts))
            return false;
        return true;
    }

}
