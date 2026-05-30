package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.Objects;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ScoringGenerationDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringGenerationPort;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
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
 * 2. Extract features from request, party and contracts
 * 3. Create a ScoringGenerationRequest with all required features
 * 4. Publish the payload to the scoring engine (via ScoringGenerationPort)
 *
 * This service is invoked asynchronously after a request details are served,
 * ensuring the HTTP response is not delayed by scoring generation.
 * Errors during scoring generation are logged but do not affect the request
 * flow.
 *
 * @author Lucia Fernandez Mancebo
 * @Date 03-22-2026
 */
@Slf4j
@Service
public class ScoringGenerationService {

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
     * Checks for existing scoring to prevent duplicates, loads full party data,
     * maps to ScoringGenerationRequest with all scoring features, and publishes
     * via ScoringGenerationPort using type-specific strategies.
     * Errors are logged but do not affect the calling request flow.
     *
     * @param request the request entity for which scoring should be generated
     */
    @Async
    public void generateScoring(Request request) {
        try {
            log.debug(LogMessage.SERVICE_SCORING_GENERATION_START, request.getId());

            // Check if scoring already exists
            if (scoringAlreadyExists(request.getId())) {
                log.debug(LogMessage.SERVICE_SCORING_GENERATION_ALREADY_EXISTS, request.getId());
                return;
            }

            // Load full party data
            Party party = partyPortRepository.findById(request.getPartyId());

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
