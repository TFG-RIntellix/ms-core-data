package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ArchiveSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CalculatedSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.SimulationDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.NotArchivedException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.RequestPartyMismatchException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.SimulationPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;
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
    private final RequestPortRepository requestPortRepository;
    private final SimulationDTOMapper simulationDTOMapper;

    public SimulationApplicationService(SimulationPortRepository simulationPortRepository,
            PartyPortRepository partyPortRepository,
            ScoringPortRepository scoringPortRepository,
            RequestPortRepository requestPortRepository,
            SimulationDTOMapper simulationDTOMapper) {
        this.simulationPortRepository = simulationPortRepository;
        this.partyPortRepository = partyPortRepository;
        this.scoringPortRepository = scoringPortRepository;
        this.requestPortRepository = requestPortRepository;
        this.simulationDTOMapper = simulationDTOMapper;
    }

    @Override
    public List<SimulationSummaryDTO> listSimulations(String requestId, String partyName, String partyId,
            Boolean archived) {
        log.debug(LogMessage.SERVICE_LIST_SIMULATIONS_START, requestId, partyName, partyId);

        if (archived == null) {
            archived = false;
        }

        // Retrieve simulations applying direct-field filters (requestId, partyId)
        List<Simulation> simulations = simulationPortRepository.findWithFilters(requestId, partyId, archived);
        log.debug(LogMessage.SERVICE_LIST_SIMULATIONS_RESULT, simulations.size());

        // Resolve partyName for each simulation (following SRP: party resolution at
        // application layer)
        simulations.forEach(simulation -> {
            simulation.setParty(partyPortRepository.findPartyName(simulation.getPartyId()));
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

        // Resolve base scoring for comparison (original scenario).
        // base_scoring_id is guaranteed by schema, but the scoring document could have
        // been removed.
        Scoring baseScoring = null;
        try {
            baseScoring = scoringPortRepository.findById(simulation.getBaseScoringId());
            log.debug(LogMessage.SERVICE_GET_SIMULATION_DETAILS_SCORING_FOUND, simulation.getBaseScoringId());
        } catch (EntityNotFoundException e) {
            log.warn(LogMessage.SERVICE_GET_SIMULATION_DETAILS_SCORING_NOT_FOUND, simulation.getBaseScoringId());
            // Continue without base scoring; the delta is still available in the simulation
        }

        SimulationDetailsDTO result = simulationDTOMapper.toDetailsDTO(simulation, baseScoring);
        log.debug(LogMessage.SERVICE_GET_SIMULATION_DETAILS_COMPLETE, simulationId);

        return result;
    }

    @Override
    public void updateSimulationTemplate(String simulationId, CalculatedSimulationDTO dto)
            throws IllegalArgumentException, EntityNotFoundException {

        log.debug(LogMessage.SERVICE_UPDATE_SIMULATION_TEMPLATE_START, simulationId);

        if (simulationId == null || simulationId.isBlank()) {
            log.warn(LogMessage.SERVICE_UPDATE_SIMULATION_TEMPLATE_VALIDATION_ERROR);
            throw new IllegalArgumentException("Simulation ID cannot be null or empty");
        }

        Simulation simulation = simulationPortRepository.findById(simulationId);
        log.debug(LogMessage.SERVICE_UPDATE_SIMULATION_TEMPLATE_FOUND, simulationId);

        // Apply the template changes to the existing simulation
        applyTemplateChanges(simulation, dto);

        simulationPortRepository.save(simulation);
        log.debug(LogMessage.SERVICE_UPDATE_SIMULATION_TEMPLATE_COMPLETE, simulationId);
    }

    @Override
    public void archiveSimulation(String simulationId, ArchiveSimulationDTO dto)
            throws IllegalArgumentException, EntityNotFoundException {

        log.debug(LogMessage.SERVICE_ARCHIVE_SIMULATION_START, simulationId);

        if (simulationId == null || simulationId.isBlank()) {
            log.warn(LogMessage.SERVICE_ARCHIVE_SIMULATION_VALIDATION_ERROR);
            throw new IllegalArgumentException("Simulation ID cannot be null or empty");
        }

        Simulation simulation = simulationPortRepository.findById(simulationId);
        log.debug(LogMessage.SERVICE_ARCHIVE_SIMULATION_FOUND, simulationId);

        simulation.setArchived(dto.getIsArchived());

        simulationPortRepository.save(simulation);
        log.debug(LogMessage.SERVICE_ARCHIVE_SIMULATION_COMPLETE, simulationId, dto.getIsArchived());
    }

    @Override
    public String createSimulation(CreateSimulationDTO dto) throws IllegalArgumentException {

        log.debug(LogMessage.SERVICE_CREATE_SIMULATION_START, dto.getRequestId(), dto.getScenarioName());

        // Verify that the request belongs to the specified party
        Request request = requestPortRepository.findById(dto.getRequestId());

        if (!request.getPartyId().equals(dto.getPartyId())) {

            log.warn(LogMessage.SERVICE_CREATE_SIMULATION_PARTY_MISMATCH,
                    dto.getRequestId(), dto.getPartyId(), request.getPartyId());

            throw new RequestPartyMismatchException(
                    "Request " + dto.getRequestId() + " belongs to party " + request.getPartyId()
                            + ", not to " + dto.getPartyId());
        }

        Simulation simulation = buildSimulation(dto);

        Simulation saved = simulationPortRepository.save(simulation);
        log.debug(LogMessage.SERVICE_CREATE_SIMULATION_COMPLETE, saved.getId());

        return saved.getId();
    }

    @Override
    public String deleteSimulation(String simulationId)
            throws IllegalArgumentException, EntityNotFoundException, NotArchivedException {

        log.debug(LogMessage.SERVICE_DELETE_SIMULATION_START, simulationId);

        simulationPortRepository.delete(simulationId);
        log.debug(LogMessage.SERVICE_DELETE_SIMULATION_COMPLETE, simulationId);

        return simulationId;
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
    private List<Simulation> filterByPartyName(List<Simulation> simulations, String partyName) {

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

    /**
     * Applies the changes from the UpdateSimulationTemplateDTO to the existing
     * Simulation entity. This includes updating the party and base scoring
     * references, form changes, simulated results and decisions, and the computed
     * deltas. This method does not handle validation of the input data, which is
     * assumed to be done at the controller layer before calling this method.
     * 
     * @param simulation the existing Simulation entity to update
     * @param dto        the UpdateSimulationTemplateDTO containing the new values
     *                   to apply to the simulation template
     *
     */
    private void applyTemplateChanges(Simulation simulation, CalculatedSimulationDTO dto) {
        simulation.setPartyId(dto.getPartyId());
        simulation.setBaseScoringId(dto.getBaseScoringId());

        // Form changes
        simulation.setFormChanges(dto.getFormChanges() != null
                ? new HashMap<>(dto.getFormChanges())
                : new HashMap<>());
        // Simulated results and decision
        RiskMetrics simulatedResults = new RiskMetrics(
                dto.getSimulatedPd(),
                dto.getSimulatedLgd(),
                dto.getSimulatedEad(),
                dto.getSimulatedEcl(),
                dto.getSimulatedRiskGrade());

        simulation.setSimulatedResults(simulatedResults);
        simulation.setSimulatedDecision(dto.getSimulatedDecision());

        // Deltas
        simulation.setPdChange(dto.getPdChange());
        simulation.setElChange(dto.getElChange());
        simulation.setRiskGradeChange(dto.getRiskGradeChange());
    }

    /**
     * Builds a new Simulation domain entity from the CreateSimulationDTO.
     * Maps all DTO fields to the domain model, setting the simulation date
     * to the current timestamp and initializing the archived flag to false.
     *
     * @param dto the creation DTO with all simulation data
     * @return a fully populated Simulation domain entity ready for persistence
     */
    private Simulation buildSimulation(CreateSimulationDTO dto) {
        Simulation simulation = new Simulation();

        simulation.setScenarioName(dto.getScenarioName());
        simulation.setRequestId(dto.getRequestId());
        simulation.setPartyId(dto.getPartyId());
        simulation.setBaseScoringId(dto.getBaseScoringId());
        simulation.setSimulationDate(new Date());

        simulation.setFormChanges(dto.getFormChanges() != null
                ? new HashMap<>(dto.getFormChanges())
                : new HashMap<>());

        RiskMetrics simulatedResults = new RiskMetrics(
                dto.getSimulatedPd(),
                dto.getSimulatedLgd(),
                dto.getSimulatedEad(),
                dto.getSimulatedEcl(),
                dto.getSimulatedRiskGrade());

        simulation.setSimulatedResults(simulatedResults);
        simulation.setSimulatedDecision(dto.getSimulatedDecision());

        simulation.setPdChange(dto.getPdChange());
        simulation.setElChange(dto.getElChange());
        simulation.setRiskGradeChange(dto.getRiskGradeChange());

        simulation.setArchived(false);

        return simulation;
    }
}
