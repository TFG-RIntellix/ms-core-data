package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

/**
 * Embedded entity representing the input features snapshot stored in a scoring
 * document.
 * Maps the "input_features" sub-document from the "scorings" MongoDB
 * collection.
 * Contains all features used as input for the credit risk model.
 *
 * @author Lucía Fernández Mancebo
 * @date 03/03/2026
 */
public class InputFeaturesEntity {
    private Integer age;
    private String gender;
    private String maritalStatus;
    private String education;
    private String employmentStatus;
    private Integer employmentSeniorityYears;
    private String incomeType;
    private String occupationSector;
    private Integer dependents;
    private String homeOwnership;
    private Boolean hasMortgage;
    private Double annualIncome;
    private String loanType;
    private String purpose;
    private Double interestRate;
    private Double dti;
    private Double existingObligations;
    private Double lti;
    private Integer previousDefaultsCount;
    private String productType;
    private CreditCardFieldsEntity creditCardFields;
    private LoanFieldsEntity loanFields;

    public InputFeaturesEntity() {
    }

    // Getters and Setters

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Integer getEmploymentSeniorityYears() {
        return employmentSeniorityYears;
    }

    public void setEmploymentSeniorityYears(Integer employmentSeniorityYears) {
        this.employmentSeniorityYears = employmentSeniorityYears;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public String getOccupationSector() {
        return occupationSector;
    }

    public void setOccupationSector(String occupationSector) {
        this.occupationSector = occupationSector;
    }

    public Integer getDependents() {
        return dependents;
    }

    public void setDependents(Integer dependents) {
        this.dependents = dependents;
    }

    public String getHomeOwnership() {
        return homeOwnership;
    }

    public void setHomeOwnership(String homeOwnership) {
        this.homeOwnership = homeOwnership;
    }

    public Boolean getHasMortgage() {
        return hasMortgage;
    }

    public void setHasMortgage(Boolean hasMortgage) {
        this.hasMortgage = hasMortgage;
    }

    public Double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getDti() {
        return dti;
    }

    public void setDti(Double dti) {
        this.dti = dti;
    }

    public Double getExistingObligations() {
        return existingObligations;
    }

    public void setExistingObligations(Double existingObligations) {
        this.existingObligations = existingObligations;
    }

    public Double getLti() {
        return lti;
    }

    public void setLti(Double lti) {
        this.lti = lti;
    }

    public Integer getPreviousDefaultsCount() {
        return previousDefaultsCount;
    }

    public void setPreviousDefaultsCount(Integer previousDefaultsCount) {
        this.previousDefaultsCount = previousDefaultsCount;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public CreditCardFieldsEntity getCreditCardFields() {
        return creditCardFields;
    }

    public void setCreditCardFields(CreditCardFieldsEntity creditCardFields) {
        this.creditCardFields = creditCardFields;
    }

    public LoanFieldsEntity getLoanFields() {
        return loanFields;
    }

    public void setLoanFields(LoanFieldsEntity loanFields) {
        this.loanFields = loanFields;
    }
}
