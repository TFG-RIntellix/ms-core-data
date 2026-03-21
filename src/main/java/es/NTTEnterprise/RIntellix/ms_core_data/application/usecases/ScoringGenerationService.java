package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ScoringGenerationDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringGenerationPort;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for asynchronous scoring generation.
 * 
 * Orchestrates the business logic for scoring generation:
 * 1. Check if scoring already exists for the request
 * 2. Extract features from request, party and contracts
 * 3. Create a ScoringGenerationDTO with all required features
 * 4. Publish the DTO to the scoring engine (via ScoringGenerationPort)
 * 
 * This service is invoked asynchronously after a request details are served,
 * ensuring the HTTP response is not delayed by scoring generation.
 * Errors during scoring generation are logged but do not affect the request
 * flow.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-15-2026
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
        this.scoringPortRepository = scoringPortRepository;
        this.partyPortRepository = partyPortRepository;
        this.scoringGenerationMapper = scoringGenerationMapper;
        this.scoringGenerationPort = scoringGenerationPort;
    }

    /**
     * Asynchronously generates and publishes a scoring generation request for a
     * given request.
     * 
     * This method runs in a separate thread and will not block the main request
     * flow.
     * Process:
     * 1. Check if scoring already exists to avoid duplicates
     * 2. Load full party data
     * 3. Map request and party to ScoringGenerationDTO
     * 4. Publish to scoring engine via ScoringGenerationPort
     * 
     * If any error occurs, it is logged and handled gracefully to ensure the
     * calling
     * request flow is not affected.
     * 
     * @param request the request entity for which scoring should be generated
     */
    @Async
    public void generateScoring(Request request) {
        try {
            log.debug(LogMessage.SERVICE_GET_DETAILS_START, request.getId());

            // Check if scoring already exists
            if (scoringAlreadyExists(request.getId())) {
                log.debug("Scoring already exists for request: {}. Skipping generation.", request.getId());
                return;
            }

            // Load full party data
            Party party = partyPortRepository.findById(request.getPartyId());

            // Extract features and create DTO
            ScoringGenerationDTO scoringGenerationDTO = scoringGenerationMapper.toDTO(request, party);

            // Publish to scoring engine
            scoringGenerationPort.publishScoringGenerationRequest(scoringGenerationDTO);

            log.info("Scoring generation request published for request: {}", request.getId());

        } catch (EntityNotFoundException e) {
            log.error("Entity not found during scoring generation for request: {}. Error: {}",
                    request.getId(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during scoring generation for request: {}. Error: {}",
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
