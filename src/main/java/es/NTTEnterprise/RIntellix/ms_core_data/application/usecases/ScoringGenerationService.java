package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.Date;
import java.util.List;
import java.util.Objects;



import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ScoringGenerationDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringGenerationPort;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ModelInputs;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFeature;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for asynchronous scoring generation.
 *
 * Orchestrates the business logic for scoring generation:
 * 1. Check if scoring already exists for the request
 * 2. Evaluate hard-cutoff rules (DTI, LTV, LTI) — if triggered, persist a
 *    rejected Scoring with PD=1 and skip the AI model call entirely
 * 3. Extract features from request, party and contracts
 * 4. Create a ScoringGenerationRequest with all required features
 * 5. Publish the payload to the scoring engine (via ScoringGenerationPort)
 *
 * This service is invoked asynchronously after a request details are served,
 * ensuring the HTTP response is not delayed by scoring generation.
 * Errors during scoring generation are logged but do not affect the request
 * flow.
 *
 * @author Lucía Fernández Mancebo
 * @date 22/03/2026
 */
@Slf4j
public class ScoringGenerationService {

    private static final String HARD_CUTOFF_MODEL_VERSION = "RULE_ENGINE_v1";
    private static final String HARD_CUTOFF_RISK_GRADE = "HIGH";

    /** Basel II standard unsecured LGD (loans, credit cards without collateral). */
    private static final double UNSECURED_LGD = 0.45;

    /** Haircut applied to collateral recovery for mortgages (10 % liquidation cost). */
    private static final double COLLATERAL_HAIRCUT = 0.90;

    private static final double DTI_THRESHOLD = 0.50;
    private static final double LTV_THRESHOLD = 0.80;
    private static final double LTI_THRESHOLD = 0.40;

    private final ScoringPortRepository scoringPortRepository;
    private final PartyPortRepository partyPortRepository;
    private final ScoringGenerationDTOMapper scoringGenerationMapper;
    private final ScoringGenerationPort scoringGenerationPort;

    public ScoringGenerationService(
            ScoringPortRepository scoringPortRepository,
            PartyPortRepository partyPortRepository,
            ScoringGenerationDTOMapper scoringGenerationMapper,
            ScoringGenerationPort scoringGenerationPort) {
        this.scoringPortRepository = Objects.requireNonNull(scoringPortRepository);
        this.partyPortRepository = Objects.requireNonNull(partyPortRepository);
        this.scoringGenerationMapper = Objects.requireNonNull(scoringGenerationMapper);
        this.scoringGenerationPort = Objects.requireNonNull(scoringGenerationPort);
    }

    /**
     * Asynchronously generates and publishes a scoring generation request.
     *
     * Runs in a separate thread to avoid blocking the main request flow.
     * Checks for existing scoring to prevent duplicates, evaluates hard-cutoff
     * rules (DTI / LTV / LTI), loads full party data, maps to
     * ScoringGenerationRequest with all scoring features, and publishes
     * via ScoringGenerationPort using type-specific strategies.
     * Errors are logged but do not affect the calling request flow.
     *
     * @param request the request entity for which scoring should be generated
     */

    public void generateScoring(Request request) {
        try {
            log.debug(LogMessage.SERVICE_SCORING_GENERATION_START, request.getId());

            // Check if scoring already exists
            if (scoringAlreadyExists(request.getId())) {
                log.debug(LogMessage.SERVICE_SCORING_GENERATION_ALREADY_EXISTS, request.getId());
                return;
            }

            // Load full party data
            Party party = request.getParty();

            // Evaluate hard-cutoff rules before calling the AI model
            if (evaluateHardCutoffRules(request, party)) {
                return;
            }

            // Extract features and create output payload
            ScoringGenerationRequest scoringGenerationRequest = scoringGenerationMapper.toOutputDTO(request, party);

            // Publish to scoring engine using type-specific strategy
            scoringGenerationPort.publishScoringGenerationRequest(scoringGenerationRequest);

            log.info(LogMessage.SERVICE_SCORING_GENERATION_PUBLISHED, request.getId());

        } catch (EntityNotFoundException e) {
            log.error(LogMessage.SERVICE_SCORING_GENERATION_ENTITY_NOT_FOUND,
                    request.getId(), e.getMessage());
        } catch (Exception e) {
            log.error(LogMessage.SERVICE_SCORING_GENERATION_UNEXPECTED_ERROR,
                    request.getId(), e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Hard-cutoff rule evaluation
    // -------------------------------------------------------------------------

    /**
     * Evaluates the three hard-cutoff financial ratio rules in priority order:
     * DTI (all types) → LTV (mortgages) → LTI (credit cards).
     *
     * If any rule fires, a rejected Scoring with PD=1 is persisted and the
     * method returns {@code true} so that the caller can skip the AI model call.
     *
     * @param request the credit request being evaluated
     * @param party   the party associated with the request (must include person
     *                details and active contracts)
     * @return {@code true} if a hard-cutoff rule was triggered, {@code false}
     *         otherwise
     */
    private boolean evaluateHardCutoffRules(Request request, Party party) {

        // Rule 1 — DTI > 50 % (applicable to all request types)
        Double dti = party.getPersonDetails().getGlobalDTI();
        if (dti != null && dti > DTI_THRESHOLD) {
            log.warn(LogMessage.SERVICE_SCORING_HARD_CUTOFF_DTI, dti, request.getId());
            persistHardCutoffScoring(request, "dti", dti);
            return true;
        }

        RequestType requestType = request.getRequestDetails().getRequestType();

        // Rule 2 — LTV > 80 % (mortgages only)
        if (RequestType.HIPOTECA.equals(requestType)) {
            Double ltv = request.calculateLTV();
            if (ltv != null && ltv > LTV_THRESHOLD) {
                log.warn(LogMessage.SERVICE_SCORING_HARD_CUTOFF_LTV, ltv, request.getId());
                persistHardCutoffScoring(request, "ltv", ltv);
                return true;
            }
        }

        // Rule 3 — LTI > 40 % (credit cards only)
        if (RequestType.TARJETA_CREDITO.equals(requestType)) {
            Double lti = request.calculateLTI();
            if (lti != null && lti > LTI_THRESHOLD) {
                log.warn(LogMessage.SERVICE_SCORING_HARD_CUTOFF_LTI, lti, request.getId());
                persistHardCutoffScoring(request, "lti", lti);
                return true;
            }
        }

        return false;
    }

    /**
     * Builds and persists a hard-cutoff Scoring with PD = 1.0.
     *
     * <p>Risk metrics are computed using Basel II approximations:
     * <ul>
     *   <li><b>EAD</b> — requested amount (loans/mortgages) or credit limit (cards)</li>
     *   <li><b>LGD</b> — for mortgages: {@code max(0, 1 - propertyValue/EAD) * COLLATERAL_HAIRCUT};
     *       for unsecured products: standard 45 %</li>
     *   <li><b>ECL</b> — {@code PD * LGD * EAD} (with PD = 1.0 this simplifies to LGD × EAD)</li>
     * </ul>
     *
     * @param request      the request being auto-rejected
     * @param featureName  name of the triggering ratio ("dti", "ltv", or "lti")
     * @param featureValue computed value of the triggering ratio
     */
    private void persistHardCutoffScoring(Request request, String featureName, Double featureValue) {

        double ead = calculateEAD(request);
        double lgd = calculateLGD(request, ead);
        double ecl = lgd * ead; // PD = 1.0

        RiskMetrics riskMetrics = new RiskMetrics(
                1.0,                    // PD = 1 (certain default)
                lgd,
                ead,
                ecl,
                HARD_CUTOFF_RISK_GRADE);

        RiskFeature topFeature = new RiskFeature(
                featureName,
                String.valueOf(featureValue),
                1.0,                    // maximum SHAP contribution
                "Hard-cutoff rule: " + featureName.toUpperCase() + " threshold exceeded");

        Scoring scoring = new Scoring(
                null,                   // ID assigned by MongoDB
                request.getId(),
                HARD_CUTOFF_MODEL_VERSION,
                new Date(),
                new ModelInputs(),      // empty snapshot — no AI model was called
                riskMetrics,
                null,                   // no base value (SHAP not applicable)
                List.of(topFeature));

        scoringPortRepository.save(scoring);

        log.info(LogMessage.SERVICE_SCORING_HARD_CUTOFF_PERSISTED,
                request.getId(), ead, lgd, ecl);
    }

    // -------------------------------------------------------------------------
    // Basel II helpers
    // -------------------------------------------------------------------------

    /**
     * Calculates the Exposure at Default (EAD).
     *
     * <ul>
     *   <li>Credit cards → credit limit requested</li>
     *   <li>Loans / Mortgages → requested loan amount</li>
     * </ul>
     *
     * @param request the credit request
     * @return the EAD value, or {@code 0.0} if the amount cannot be determined
     */
    private double calculateEAD(Request request) {
        RequestType type = request.getRequestDetails().getRequestType();

        if (RequestType.TARJETA_CREDITO.equals(type)) {
            var creditLimit = request.getRequestDetails().getCreditLimit();
            if (creditLimit != null && creditLimit.getAmount() != null) {
                return creditLimit.getAmount();
            }
        } else {
            var requestedAmount = request.getRequestDetails().getRequestedAmount();
            if (requestedAmount != null && requestedAmount.getAmount() != null) {
                return requestedAmount.getAmount();
            }
        }
        return 0.0;
    }

    /**
     * Calculates the Loss Given Default (LGD) using Basel II approximations.
     *
     * <ul>
     *   <li>Mortgages with collateral: {@code max(0, 1 - propertyValue/EAD) * COLLATERAL_HAIRCUT}</li>
     *   <li>All other products: standard unsecured LGD of {@value #UNSECURED_LGD}</li>
     * </ul>
     *
     * @param request the credit request
     * @param ead     the previously computed EAD
     * @return the LGD value in the [0, 1] range
     */
    private double calculateLGD(Request request, double ead) {
        if (RequestType.HIPOTECA.equals(request.getRequestDetails().getRequestType())
                && request.getCollateral() != null
                && request.getCollateral().getPropertyValue() != null
                && request.getCollateral().getPropertyValue().getAmount() != null
                && ead > 0) {

            double propertyValue = request.getCollateral().getPropertyValue().getAmount();
            double rawLgd = Math.max(0.0, 1.0 - (propertyValue / ead));
            return rawLgd * COLLATERAL_HAIRCUT;
        }
        return UNSECURED_LGD;
    }

    // -------------------------------------------------------------------------
    // Duplicate-check helper
    // -------------------------------------------------------------------------

    /**
     * Checks if a scoring already exists for the given request ID.
     *
     * @param requestId the request ID to check
     * @return true if scoring exists, false otherwise
     */
    private boolean scoringAlreadyExists(String requestId) {
        try {
            scoringPortRepository.findByRequestId(requestId);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }
}
