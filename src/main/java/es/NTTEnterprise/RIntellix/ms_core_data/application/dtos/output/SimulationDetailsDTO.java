package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output;

import java.util.Map;

/**
 * Data Transfer Object (DTO) for the detailed view of a simulation.
 * Contains the modified input values, the recalculated risk metrics,
 * the original (base) scoring results and the computed deltas for comparison.
 *
 * @author Lucía Fernández Mancebo
 * @date 03/03/2026
 */
public class SimulationDetailsDTO {

    private String simulationId;
    private String scenarioName;
    private String simulationDate;
    private String requestId;
    private String requestCode;
    private String baseScoringId;

    // Modified values applied in the simulation
    private Map<String, Object> formChanges;

    // Base scoring results (original scenario)
    private Double basePd;
    private Double baseLgd;
    private Double baseEad;
    private Double baseEcl;
    private String baseRiskGrade;

    // Simulated results (scenario with changes applied)
    private Double simulatedPd;
    private Double simulatedLgd;
    private Double simulatedEad;
    private Double simulatedEcl;
    private String simulatedRiskGrade;
    private String simulatedDecision;

    // Delta (comparison between base and simulated)
    private Double pdChange;
    private Double elChange;
    private String riskGradeChange;

    private SimulatedResults simulatedResults;
    private Delta delta;

    public SimulationDetailsDTO() {
    }

    // Getters and Setters

    public String getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(String simulationId) {
        this.simulationId = simulationId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getSimulationDate() {
        return simulationDate;
    }

    public void setSimulationDate(String simulationDate) {
        this.simulationDate = simulationDate;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getBaseScoringId() {
        return baseScoringId;
    }

    public void setBaseScoringId(String baseScoringId) {
        this.baseScoringId = baseScoringId;
    }

    public Map<String, Object> getFormChanges() {
        return formChanges;
    }

    public void setFormChanges(Map<String, Object> formChanges) {
        this.formChanges = formChanges;
    }

    public Double getBasePd() {
        return basePd;
    }

    public void setBasePd(Double basePd) {
        this.basePd = basePd;
    }

    public Double getBaseLgd() {
        return baseLgd;
    }

    public void setBaseLgd(Double baseLgd) {
        this.baseLgd = baseLgd;
    }

    public Double getBaseEad() {
        return baseEad;
    }

    public void setBaseEad(Double baseEad) {
        this.baseEad = baseEad;
    }

    public Double getBaseEcl() {
        return baseEcl;
    }

    public void setBaseEcl(Double baseEcl) {
        this.baseEcl = baseEcl;
    }

    public String getBaseRiskGrade() {
        return baseRiskGrade;
    }

    public void setBaseRiskGrade(String baseRiskGrade) {
        this.baseRiskGrade = baseRiskGrade;
    }

    public Double getSimulatedPd() {
        return simulatedPd;
    }

    public void setSimulatedPd(Double simulatedPd) {
        this.simulatedPd = simulatedPd;
    }

    public Double getSimulatedLgd() {
        return simulatedLgd;
    }

    public void setSimulatedLgd(Double simulatedLgd) {
        this.simulatedLgd = simulatedLgd;
    }

    public Double getSimulatedEad() {
        return simulatedEad;
    }

    public void setSimulatedEad(Double simulatedEad) {
        this.simulatedEad = simulatedEad;
    }

    public Double getSimulatedEcl() {
        return simulatedEcl;
    }

    public void setSimulatedEcl(Double simulatedEcl) {
        this.simulatedEcl = simulatedEcl;
    }

    public String getSimulatedRiskGrade() {
        return simulatedRiskGrade;
    }

    public void setSimulatedRiskGrade(String simulatedRiskGrade) {
        this.simulatedRiskGrade = simulatedRiskGrade;
    }

    public String getSimulatedDecision() {
        return simulatedDecision;
    }

    public void setSimulatedDecision(String simulatedDecision) {
        this.simulatedDecision = simulatedDecision;
    }

    public Double getPdChange() {
        return pdChange;
    }

    public void setPdChange(Double pdChange) {
        this.pdChange = pdChange;
    }

    public Double getElChange() {
        return elChange;
    }

    public void setElChange(Double elChange) {
        this.elChange = elChange;
    }

    public String getRiskGradeChange() {
        return riskGradeChange;
    }

    public void setRiskGradeChange(String riskGradeChange) {
        this.riskGradeChange = riskGradeChange;
    }

    public SimulatedResults getSimulatedResults() {
        return simulatedResults;
    }

    public void setSimulatedResults(SimulatedResults simulatedResults) {
        this.simulatedResults = simulatedResults;
    }

    public Delta getDelta() {
        return delta;
    }

    public void setDelta(Delta delta) {
        this.delta = delta;
    }

    public static class SimulatedResults {
        private Double pd;
        private Double lgd;
        private Double ead;
        private Double ecl;
        private String riskGrade;
        private String decision;
        private Double monthlyPayment;
        private Double dti;
        private Double totalPayment;
        private Double totalInterest;
        private Double disposableIncome;

        public Double getPd() { return pd; }
        public void setPd(Double pd) { this.pd = pd; }

        public Double getLgd() { return lgd; }
        public void setLgd(Double lgd) { this.lgd = lgd; }

        public Double getEad() { return ead; }
        public void setEad(Double ead) { this.ead = ead; }

        public Double getEcl() { return ecl; }
        public void setEcl(Double ecl) { this.ecl = ecl; }

        public String getRiskGrade() { return riskGrade; }
        public void setRiskGrade(String riskGrade) { this.riskGrade = riskGrade; }

        public String getDecision() { return decision; }
        public void setDecision(String decision) { this.decision = decision; }

        public Double getMonthlyPayment() { return monthlyPayment; }
        public void setMonthlyPayment(Double monthlyPayment) { this.monthlyPayment = monthlyPayment; }

        public Double getDti() { return dti; }
        public void setDti(Double dti) { this.dti = dti; }

        public Double getTotalPayment() { return totalPayment; }
        public void setTotalPayment(Double totalPayment) { this.totalPayment = totalPayment; }

        public Double getTotalInterest() { return totalInterest; }
        public void setTotalInterest(Double totalInterest) { this.totalInterest = totalInterest; }

        public Double getDisposableIncome() { return disposableIncome; }
        public void setDisposableIncome(Double disposableIncome) { this.disposableIncome = disposableIncome; }
    }

    public static class Delta {
        private Double pdChange;
        private Double eclChange;
        private String riskGradeChange;
        private Double monthlyPaymentChange;
        private Double dtiChange;
        private Double totalPaymentChange;
        private Double totalInterestChange;
        private Double monthlyDisposableIncomeChange;

        public Double getPdChange() { return pdChange; }
        public void setPdChange(Double pdChange) { this.pdChange = pdChange; }

        public Double getEclChange() { return eclChange; }
        public void setEclChange(Double eclChange) { this.eclChange = eclChange; }

        public String getRiskGradeChange() { return riskGradeChange; }
        public void setRiskGradeChange(String riskGradeChange) { this.riskGradeChange = riskGradeChange; }

        public Double getMonthlyPaymentChange() { return monthlyPaymentChange; }
        public void setMonthlyPaymentChange(Double monthlyPaymentChange) { this.monthlyPaymentChange = monthlyPaymentChange; }

        public Double getDtiChange() { return dtiChange; }
        public void setDtiChange(Double dtiChange) { this.dtiChange = dtiChange; }

        public Double getTotalPaymentChange() { return totalPaymentChange; }
        public void setTotalPaymentChange(Double totalPaymentChange) { this.totalPaymentChange = totalPaymentChange; }

        public Double getTotalInterestChange() { return totalInterestChange; }
        public void setTotalInterestChange(Double totalInterestChange) { this.totalInterestChange = totalInterestChange; }

        public Double getMonthlyDisposableIncomeChange() { return monthlyDisposableIncomeChange; }
        public void setMonthlyDisposableIncomeChange(Double monthlyDisposableIncomeChange) { this.monthlyDisposableIncomeChange = monthlyDisposableIncomeChange; }
    }
}
