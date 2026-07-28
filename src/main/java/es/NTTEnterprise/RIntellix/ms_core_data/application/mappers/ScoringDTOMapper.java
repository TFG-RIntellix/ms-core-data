package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.TopFeatureDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFeature;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;

/**
 * Mapper class to convert between Scoring (domain) and ScoringDTO
 * (application).
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public class ScoringDTOMapper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Converts a Scoring domain entity into a ScoringDTO.
     * 
     * @param scoring The domain Scoring entity.
     * @return The ScoringDTO ready for the REST response.
     */
    public ScoringDTO toDTO(Scoring scoring) {
        if (scoring == null) {
            return null;
        }

        ScoringDTO dto = new ScoringDTO();
        dto.setScoringId(scoring.getId());
        dto.setRequestId(scoring.getRequestId());
        dto.setModelVersion(scoring.getModelVersion());
        dto.setScoringDate(DATE_FORMAT.format(scoring.getExecutionDate()));

        // Input features (already a HashMap in domain) and remapping of isRevolving
        // field to "Si"/"No"
        Map<String, Object> inputFeatures = scoring.getInputSnapshot().getFeatures();
        inputFeatures.put("is_revolving", Boolean.TRUE.equals(inputFeatures.get("is_revolving")) ? "Si" : "No");
        dto.setInputFeatures(inputFeatures);

        // Risk metrics
        dto.setPd(scoring.getResults().getProbabilityOfDefault());
        dto.setLgd(scoring.getResults().getLossGivenDefault());
        dto.setEad(scoring.getResults().getExposureAtDefault());
        dto.setEcl(scoring.getResults().getExpectedCalculatedLoss());
        dto.setRiskGrade(scoring.getResults().getRiskLevel());
        dto.setMonthlyPayment(scoring.getResults().getFinancialMetrics().getMonthlyPayment());
        dto.setDti(scoring.getResults().getFinancialMetrics().getDebtToIncomeRatio());
        dto.setTotalPayment(scoring.getResults().getFinancialMetrics().getTotalPayment());
        dto.setTotalInterest(scoring.getResults().getFinancialMetrics().getTotalInterest());
        dto.setMonthlyDisposableIncome(scoring.getResults().getFinancialMetrics().getMonthlyDisposableIncome());

        // Explainability
        dto.setBaseValue(scoring.getBaseValue());
        dto.setTopFeatures(mapTopFeatures(scoring.getExplainability()));

        return dto;
    }

    private List<TopFeatureDTO> mapTopFeatures(List<RiskFeature> features) {
        if (features == null || features.isEmpty()) {
            return new ArrayList<>();
        }
        return features.stream()
                .map(f -> new TopFeatureDTO(f.getFeatureName(), f.getFeatureValue(), f.getShapValue(), f.getDescription()))
                .collect(Collectors.toList());
    }
}
