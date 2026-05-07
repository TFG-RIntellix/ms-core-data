package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.NTTEnterprise.RIntellix.ms_core_data.application.constraints.NonNegativeAmount;
import es.NTTEnterprise.RIntellix.ms_core_data.application.constraints.ValidAge;
import es.NTTEnterprise.RIntellix.ms_core_data.application.constraints.ValidPercentage;

/**
 * DTO representing the input features section of a Kafka scoring message.
 * Contains all demographic and financial data used as input for the scoring
 * model.
 * Validation constraints are applied directly on fields to ensure data
 * integrity.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
public class InputFeaturesDTO {

    @JsonProperty("edad")
    @ValidAge(minAge = 18, message = "Age must be at least 18")
    private Integer age;

    @JsonProperty("genero")
    private String gender;

    @JsonProperty("estado_civil")
    private String maritalStatus;

    @JsonProperty("educacion")
    private String education;

    @JsonProperty("situacion_laboral")
    private String employmentStatus;

    @JsonProperty("sector_trabajo")
    private String workSector;

    @JsonProperty("dependientes")
    private Integer nrDependants;

    @JsonProperty("vivienda")
    private String homeOwnership;

    @JsonProperty("tiene_hipoteca")
    private String hasMortgage;

    @JsonProperty("ingresos_anuales")
    @NonNegativeAmount(message = "Annual income cannot be negative")
    private Double annualIncome;

    @JsonProperty("tipo_prestamo")
    private String requestType;

    @JsonProperty("proposito")
    private String purpose;

    @JsonProperty("monto_prestamo")
    @NonNegativeAmount(message = "Requested amount cannot be negative")
    private Double requestedAmount;

    @JsonProperty("plazo_meses")
    private Integer termMonths;

    @JsonProperty("tasa_interes")
    @NonNegativeAmount(message = "Interest rate cannot be negative")
    private Double interestRate;

    @JsonProperty("ltv")
    @ValidPercentage(message = "LTV must be between 0 and 1")
    private Double ltv;

    @JsonProperty("dti")
    @ValidPercentage(message = "DTI must be between 0 and 1")
    private Double dti;

    @JsonProperty("num_prestamos_previos")
    private Integer previousLoansCount;

    @JsonProperty("num_moras_previas")
    private Integer previousDefaultsCount;

    /**
     * Default constructor for InputFeaturesDTO.
     */
    public InputFeaturesDTO() {
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

    public String getWorkSector() {
        return workSector;
    }

    public void setWorkSector(String workSector) {
        this.workSector = workSector;
    }

    public Integer getNrDependants() {
        return nrDependants;
    }

    public void setNrDependants(Integer nrDependants) {
        this.nrDependants = nrDependants;
    }

    public String getHomeOwnership() {
        return homeOwnership;
    }

    public void setHomeOwnership(String homeOwnership) {
        this.homeOwnership = homeOwnership;
    }

    public String getHasMortgage() {
        return hasMortgage;
    }

    public void setHasMortgage(String hasMortgage) {
        this.hasMortgage = hasMortgage;
    }

    public Double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

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

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getLtv() {
        return ltv;
    }

    public void setLtv(Double ltv) {
        this.ltv = ltv;
    }

    public Double getDti() {
        return dti;
    }

    public void setDti(Double dti) {
        this.dti = dti;
    }

    public Integer getPreviousLoansCount() {
        return previousLoansCount;
    }

    public void setPreviousLoansCount(Integer previousLoansCount) {
        this.previousLoansCount = previousLoansCount;
    }

    public Integer getPreviousDefaultsCount() {
        return previousDefaultsCount;
    }

    public void setPreviousDefaultsCount(Integer previousDefaultsCount) {
        this.previousDefaultsCount = previousDefaultsCount;
    }
}
