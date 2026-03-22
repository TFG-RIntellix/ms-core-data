package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.Objects;

import org.springframework.stereotype.Service;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ScoringDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.ScoringPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Implements ScoringPortService interface handling business logic for scoring
 * retrieval.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Slf4j
@Service
public class ScoringApplicationService implements ScoringPortService {

    private static final String INVALID_REQUEST_ID_MESSAGE = "Request ID cannot be null or empty";

    private final ScoringPortRepository scoringPortRepository;
    private final ScoringDTOMapper scoringDTOMapper;

    public ScoringApplicationService(ScoringPortRepository scoringPortRepository,
            ScoringDTOMapper scoringDTOMapper) {
        this.scoringPortRepository = Objects.requireNonNull(scoringPortRepository);
        this.scoringDTOMapper = Objects.requireNonNull(scoringDTOMapper);
    }

    @Override
    public ScoringDTO getScoringByRequestId(String requestId)
            throws IllegalArgumentException, EntityNotFoundException {

        log.debug(LogMessage.SERVICE_GET_SCORING_START, requestId);
        log.debug(LogMessage.SERVICE_GET_SCORING_VALIDATION, requestId);

        if (requestId == null || requestId.isBlank()) {
            log.warn(LogMessage.SERVICE_GET_SCORING_VALIDATION_ERROR);
            throw new IllegalArgumentException(INVALID_REQUEST_ID_MESSAGE);
        }

        Scoring scoring = scoringPortRepository.findByRequestId(requestId);
        log.debug(LogMessage.SERVICE_GET_SCORING_FOUND, requestId);

        ScoringDTO result = scoringDTOMapper.toDTO(scoring);
        log.debug(LogMessage.SERVICE_GET_SCORING_COMPLETE, requestId);

        return result;
    }
}
