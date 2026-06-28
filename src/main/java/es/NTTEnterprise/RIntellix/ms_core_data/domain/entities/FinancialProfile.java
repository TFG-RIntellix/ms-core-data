package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.EmploymentStatus;

/**
 * Value object representing the financial profile of a person.
 * Contains income, employment and credit history information.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public class FinancialProfile {

    private Money annualIncome;
    private EmploymentStatus employmentStatus;
    private String occupationSector;
    private Double seniorityYears;

    // Credit History
    private Integer previousDefaultsCount;
    private Money previousRepaymentsAmount;
    private Integer previousLoansCount;
    private Money previousLoansAmount;
    private Boolean isNewCustomer;

    private Double existingObligations;
    private Boolean hasMortage;

    /**
     * Constructor of the FinancialProfile class.
     */
    public FinancialProfile() {
    }

    /**
     * Constructor of the FinancialProfile class.
     * 
     * @param annualIncome             the annual income of the person
     * @param employmentStatus         the employment status of the person
     * @param occupationSector         the occupation sector of the person
     * @param seniorityYears           the number of years of seniority
     * @param previousDefaultsCount    the count of previous defaults
     * @param previousRepaymentsAmount the amount of previous repayments
     * @param previousLoansCount       the count of previous loans
     * @param previousLoansAmount      the amount of previous loans
     * @param isNewCustomer            indicates if the person is a new customer (no
     *                                 credit history)
     */
    public FinancialProfile(Money annualIncome, EmploymentStatus employmentStatus, String occupationSector,
            Double seniorityYears, Integer previousDefaultsCount, Money previousRepaymentsAmount,
            Integer previousLoansCount, Money previousLoansAmount, Boolean isNewCustomer) {
        this.annualIncome = annualIncome;
        this.employmentStatus = employmentStatus;
        this.occupationSector = occupationSector;
        this.seniorityYears = seniorityYears;
        this.previousDefaultsCount = previousDefaultsCount;
        this.previousRepaymentsAmount = previousRepaymentsAmount;
        this.previousLoansCount = previousLoansCount;
        this.previousLoansAmount = previousLoansAmount;
        this.isNewCustomer = isNewCustomer;
    }

    // Getters and Setters

    public Money getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Money annualIncome) {
        this.annualIncome = annualIncome;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getOccupationSector() {
        return occupationSector;
    }

    public void setOccupationSector(String occupationSector) {
        this.occupationSector = occupationSector;
    }

    public Double getSeniorityYears() {
        return seniorityYears;
    }

    public void setSeniorityYears(Double seniorityYears) {
        this.seniorityYears = seniorityYears;
    }

    public Integer getPreviousDefaultsCount() {
        return previousDefaultsCount;
    }

    public void setPreviousDefaultsCount(Integer previousDefaultsCount) {
        this.previousDefaultsCount = previousDefaultsCount;
    }

    public Money getPreviousRepaymentsAmount() {
        return previousRepaymentsAmount;
    }

    public void setPreviousRepaymentsAmount(Money previousRepaymentsAmount) {
        this.previousRepaymentsAmount = previousRepaymentsAmount;
    }

    public Integer getPreviousLoansCount() {
        return previousLoansCount;
    }

    public void setPreviousLoansCount(Integer previousLoansCount) {
        this.previousLoansCount = previousLoansCount;
    }

    public Money getPreviousLoansAmount() {
        return previousLoansAmount;
    }

    public void setPreviousLoansAmount(Money previousLoansAmount) {
        this.previousLoansAmount = previousLoansAmount;
    }

    public Boolean getIsNewCustomer() {
        return isNewCustomer;
    }

    public void setIsNewCustomer(Boolean isNewCustomer) {
        this.isNewCustomer = isNewCustomer;
    }

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

    // toString, hashCode and equals
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FinancialProfile{");
        sb.append("annualIncome=").append(annualIncome);
        sb.append(", employmentStatus=").append(employmentStatus);
        sb.append(", occupationSector=").append(occupationSector);
        sb.append(", seniorityYears=").append(seniorityYears);
        sb.append(", previousDefaultsCount=").append(previousDefaultsCount);
        sb.append(", previousRepaymentsAmount=").append(previousRepaymentsAmount);
        sb.append(", previousLoansCount=").append(previousLoansCount);
        sb.append(", previousLoansAmount=").append(previousLoansAmount);
        sb.append(", isNewCustomer=").append(isNewCustomer);
        sb.append(", existingObligations=").append(existingObligations);
        sb.append(", hasMortage=").append(hasMortage);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((annualIncome == null) ? 0 : annualIncome.hashCode());
        result = prime * result + ((employmentStatus == null) ? 0 : employmentStatus.hashCode());
        result = prime * result + ((occupationSector == null) ? 0 : occupationSector.hashCode());
        result = prime * result + ((seniorityYears == null) ? 0 : seniorityYears.hashCode());
        result = prime * result + ((previousDefaultsCount == null) ? 0 : previousDefaultsCount.hashCode());
        result = prime * result + ((previousRepaymentsAmount == null) ? 0 : previousRepaymentsAmount.hashCode());
        result = prime * result + ((previousLoansCount == null) ? 0 : previousLoansCount.hashCode());
        result = prime * result + ((previousLoansAmount == null) ? 0 : previousLoansAmount.hashCode());
        result = prime * result + ((isNewCustomer == null) ? 0 : isNewCustomer.hashCode());
        result = prime * result + ((existingObligations == null) ? 0 : existingObligations.hashCode());
        result = prime * result + ((hasMortage == null) ? 0 : hasMortage.hashCode());
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
        FinancialProfile other = (FinancialProfile) obj;
        if (annualIncome == null) {
            if (other.annualIncome != null)
                return false;
        } else if (!annualIncome.equals(other.annualIncome))
            return false;
        if (employmentStatus != other.employmentStatus)
            return false;
        if (occupationSector == null) {
            if (other.occupationSector != null)
                return false;
        } else if (!occupationSector.equals(other.occupationSector))
            return false;
        if (seniorityYears == null) {
            if (other.seniorityYears != null)
                return false;
        } else if (!seniorityYears.equals(other.seniorityYears))
            return false;
        if (previousDefaultsCount == null) {
            if (other.previousDefaultsCount != null)
                return false;
        } else if (!previousDefaultsCount.equals(other.previousDefaultsCount))
            return false;
        if (previousRepaymentsAmount == null) {
            if (other.previousRepaymentsAmount != null)
                return false;
        } else if (!previousRepaymentsAmount.equals(other.previousRepaymentsAmount))
            return false;
        if (previousLoansCount == null) {
            if (other.previousLoansCount != null)
                return false;
        } else if (!previousLoansCount.equals(other.previousLoansCount))
            return false;
        if (previousLoansAmount == null) {
            if (other.previousLoansAmount != null)
                return false;
        } else if (!previousLoansAmount.equals(other.previousLoansAmount))
            return false;
        if (isNewCustomer == null) {
            if (other.isNewCustomer != null)
                return false;
        } else if (!isNewCustomer.equals(other.isNewCustomer))
            return false;
        if (existingObligations == null) {
            if (other.existingObligations != null)
                return false;
        } else if (!existingObligations.equals(other.existingObligations))
            return false;
        if (hasMortage == null) {
            if (other.hasMortage != null)
                return false;
        } else if (!hasMortage.equals(other.hasMortage))
            return false;
        return true;
    }
}
