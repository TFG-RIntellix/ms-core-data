package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output;

/**
 * Output DTO for scoring generation publication.
 *
 * This class represents the transport payload prepared by the application
 * layer and sent through the scoring publication port to infrastructure
 * adapters (e.g., Kafka).
 *
 * @author Lucia Fernandez Mancebo
 * @Date 03-22-2026
 */
public class ScoringGenerationRequest {

    private String requestId;
    private String partyId;

    // Socio-demographic features
    private Integer age;
    private String gender;
    private String maritalStatus;
    private String education;
    private Integer dependents;

    private String homeOwnership;
    private Boolean hasMortgage;

    // Employment features
    private String employmentStatus;
    private String occupationSector;
    private Double employmentSeniorityYears;

    // Financial features
    private Double annualIncome;
    private String incomeType;
    private Double existingObligations;

    // Loan/Request features
    private String requestType;
    private String purpose;
    private Double loanAmount;
    private Integer termMonths;
    private Double interestRate;
    private String loanType;

    // Credit card specific features
    private Double creditLimit;
    private Boolean isRevolving;

    // Risk/Credit history features
    private Double lti;
    private Double ltv;
    private Double dti;
    private Integer previousLoansCount;
    private Integer previousDefaultsCount;

    /**
     * Creates an empty scoring generation request payload.
     */
    public ScoringGenerationRequest() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(final String partyId) {
        this.partyId = partyId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(final String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(final String education) {
        this.education = education;
    }

    public Integer getDependents() {
        return dependents;
    }

    public void setDependents(final Integer dependents) {
        this.dependents = dependents;
    }

    public String getHomeOwnership() {
        return homeOwnership;
    }

    public void setHomeOwnership(final String homeOwnership) {
        this.homeOwnership = homeOwnership;
    }

    public Boolean getHasMortgage() {
        return hasMortgage;
    }

    public void setHasMortgage(final Boolean hasMortgage) {
        this.hasMortgage = hasMortgage;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(final String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Double getEmploymentSeniorityYears() {
        return employmentSeniorityYears;
    }

    public void setEmploymentSeniorityYears(final Double employmentSeniorityYears) {
        this.employmentSeniorityYears = employmentSeniorityYears;
    }

    public String getOccupationSector() {
        return occupationSector;
    }

    public void setOccupationSector(final String occupationSector) {
        this.occupationSector = occupationSector;
    }

    public Double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(final Double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(final String incomeType) {
        this.incomeType = incomeType;
    }

    public Double getExistingObligations() {
        return existingObligations;
    }

    public void setExistingObligations(final Double existingObligations) {
        this.existingObligations = existingObligations;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(final String requestType) {
        this.requestType = requestType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(final String purpose) {
        this.purpose = purpose;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(final Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(final Integer termMonths) {
        this.termMonths = termMonths;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(final Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(final Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Boolean getIsRevolving() {
        return isRevolving;
    }

    public void setIsRevolving(final Boolean isRevolving) {
        this.isRevolving = isRevolving;
    }

    public Double getLtv() {
        return ltv;
    }

    public void setLtv(final Double ltv) {
        this.ltv = ltv;
    }

    public Double getDti() {
        return dti;
    }

    public void setDti(final Double dti) {
        this.dti = dti;
    }

    public Double getLti() {
        return lti;
    }

    public void setLti(final Double lti) {
        this.lti = lti;
    }

    public Integer getPreviousLoansCount() {
        return previousLoansCount;
    }

    public void setPreviousLoansCount(final Integer previousLoansCount) {
        this.previousLoansCount = previousLoansCount;
    }

    public Integer getPreviousDefaultsCount() {
        return previousDefaultsCount;
    }

    public void setPreviousDefaultsCount(final Integer previousDefaultsCount) {
        this.previousDefaultsCount = previousDefaultsCount;
    }

    @Override
    public String toString() {
        return "ScoringGenerationRequest{" +
                "requestId='" + requestId + '\'' +
                ", partyId='" + partyId + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", education='" + education + '\'' +
                ", dependents=" + dependents +
                ", homeOwnership='" + homeOwnership + '\'' +
                ", hasMortgage=" + hasMortgage +
                ", employmentStatus='" + employmentStatus + '\'' +
                ", occupationSector='" + occupationSector + '\'' +
                ", employmentSeniorityYears=" + employmentSeniorityYears +
                ", annualIncome=" + annualIncome +
                ", incomeType='" + incomeType + '\'' +
                ", existingObligations=" + existingObligations +
                ", requestType='" + requestType + '\'' +
                ", purpose='" + purpose + '\'' +
                ", loanAmount=" + loanAmount +
                ", termMonths=" + termMonths +
                ", interestRate=" + interestRate +
                ", creditLimit=" + creditLimit +
                ", isRevolving=" + isRevolving +
                ", ltv=" + ltv +
                ", dti=" + dti +
                ", lti=" + lti +
                ", previousLoansCount=" + previousLoansCount +
                ", previousDefaultsCount=" + previousDefaultsCount +
                '}';
    }
}