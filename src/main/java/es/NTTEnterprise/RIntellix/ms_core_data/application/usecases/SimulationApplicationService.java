package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.SimulationDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.SimulationPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.SimulationPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Implements SimulationPortService interface handling business logic for
 * simulation retrieval.
 * Orchestrates simulation, party and scoring repositories to compose the
 * required DTOs.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Slf4j
@Service
public class SimulationApplicationService implements SimulationPortService {

    private final SimulationPortRepository simulationPortRepository;
    private final PartyPortRepository partyPortRepository;
    private final ScoringPortRepository scoringPortRepository;
    private final SimulationDTOMapper simulationDTOMapper;

    public SimulationApplicationService(SimulationPortRepository simulationPortRepository,
            PartyPortRepository partyPortRepository,
            ScoringPortRepository scoringPortRepository,
            SimulationDTOMapper simulationDTOMapper) {
        this.simulationPortRepository = simulationPortRepository;
        this.partyPortRepository = partyPortRepository;
        this.scoringPortRepository = scoringPortRepository;
        this.simulationDTOMapper = simulationDTOMapper;
    }

    @Override
    public List<SimulationSummaryDTO> listSimulations(String requestId, String partyName, String partyId) {
        log.debug(LogMessage.SERVICE_LIST_SIMULATIONS_START, requestId, partyName, partyId);

        // Retrieve simulations applying direct-field filters (requestId, partyId)
        List<Simulation> simulations = simulationPortRepository.findWithFilters(requestId, partyId);
        log.debug(LogMessage.SERVICE_LIST_SIMULATIONS_RESULT, simulations.size());

        // Resolve partyName for each simulation (following SRP: party resolution at
        // application layer)
        simulations.forEach(simulation -> {
            if (simulation.getPartyId() != null) {
                simulation.setParty(partyPortRepository.findPartyName(simulation.getPartyId()));
            }
        });

        simulations = filterByPartyName(simulations, partyName);

        log.debug(LogMessage.SERVICE_LIST_SIMULATIONS_MAPPING, simulations.size());
        return simulations.stream()
                .map(simulationDTOMapper::toSummaryDTO)
                .toList();
    }

    @Override
    public SimulationDetailsDTO getSimulationDetails(String simulationId)
            throws IllegalArgumentException, EntityNotFoundException {

        log.debug(LogMessage.SERVICE_GET_SIMULATION_DETAILS_START, simulationId);
        log.debug(LogMessage.SERVICE_GET_SIMULATION_DETAILS_VALIDATION, simulationId);

        if (simulationId == null || simulationId.isBlank()) {
            log.warn(LogMessage.SERVICE_GET_SIMULATION_DETAILS_VALIDATION_ERROR);
            throw new IllegalArgumentException("Simulation ID cannot be null or empty");
        }

        Simulation simulation = simulationPortRepository.findById(simulationId);
        log.debug(LogMessage.SERVICE_GET_SIMULATION_DETAILS_FOUND, simulationId);

        // TODO: Resolve base scoring for comparison (original scenario), tocheck:
        // because it should be always persisted, the baseScoring shuldn't be null.
        Scoring baseScoring = null;
        if (simulation.getBaseScoringId() != null) {
            try {
                baseScoring = scoringPortRepository.findById(simulation.getBaseScoringId());
                log.debug(LogMessage.SERVICE_GET_SIMULATION_DETAILS_SCORING_FOUND, simulation.getBaseScoringId());
            } catch (EntityNotFoundException e) {
                log.warn(LogMessage.SERVICE_GET_SIMULATION_DETAILS_SCORING_NOT_FOUND, simulation.getBaseScoringId());
                // Continue without base scoring; the delta is still available in the simulation
            }
        }

        SimulationDetailsDTO result = simulationDTOMapper.toDetailsDTO(simulation, baseScoring);
        log.debug(LogMessage.SERVICE_GET_SIMULATION_DETAILS_COMPLETE, simulationId);

        return result;
    }

    /**
     * Filters a list of simulations by party name (case-insensitive, partial
     * match).
     * This is a post-retrieval filter since party name is not stored in the
     * simulations
     * collection and must be resolved separately to respect SRP. If partyName is
     * null or blank,
     * the original list is returned unfiltered.
     * 
     * @param simulations the list of simulations to filter
     * @param partyName   the party name to filter by (optional)
     * @return a list of simulations whose associated party's full name contains the
     *         given partyName
     */
    public List<Simulation> filterByPartyName(List<Simulation> simulations, String partyName) {

        List<Simulation> filteredSimulations = null;

        if (partyName == null || partyName.isBlank()) {
            return simulations;
        }

        log.debug(LogMessage.SERVICE_LIST_SIMULATIONS_FILTERING_BY_NAME, partyName);

        // By contract we assume that a simulation always has a party associated with a
        // full name.
        filteredSimulations = simulations.stream().filter(
                simulation -> simulation.getParty().getPersonDetails().getFullName()
                        .toLowerCase().contains(partyName.toLowerCase()))
                .toList();

        log.debug(LogMessage.SERVICE_LIST_SIMULATIONS_AFTER_FILTER, simulations.size());
        return filteredSimulations;
    }
}
