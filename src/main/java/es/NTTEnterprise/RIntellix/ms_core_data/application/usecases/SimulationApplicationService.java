package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialMetrics;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ArchiveSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CalculatedSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.SimulationDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.DuplicateSimulationNameException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.NotArchivedException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.RequestPartyMismatchException;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.SimulationPortService;
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
public class SimulationApplicationService implements SimulationPortService {

    private static final String INVALID_SIMULATION_ID_MESSAGE = "Simulation ID cannot be null or empty";

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
        this.simulationPortRepository = Objects.requireNonNull(simulationPortRepository);
        this.partyPortRepository = Objects.requireNonNull(partyPortRepository);
        this.scoringPortRepository = Objects.requireNonNull(scoringPortRepository);
        this.requestPortRepository = Objects.requireNonNull(requestPortRepository);
        this.simulationDTOMapper = Objects.requireNonNull(simulationDTOMapper);
    }

    @Override
    public List<SimulationSummaryDTO> listSimulations(String search, Boolean archived) {
        log.debug(LogMessage.SERVICE_LIST_SIMULATIONS_START, search);

        // Resolve matching party IDs based on the search string
        Set<String> matchingPartyIds = null;
        if (search != null && !search.isBlank()) {
            matchingPartyIds = partyPortRepository.findPartyIdsByNameMatch(search);
        }

        // Retrieve simulations applying the generic search and party IDs
        List<String> partyIdsList = matchingPartyIds != null ? matchingPartyIds.stream().toList() : null;
        List<Simulation> simulations = simulationPortRepository.findWithFilters(search, partyIdsList, archived);
        log.debug(LogMessage.SERVICE_LIST_SIMULATIONS_RESULT, simulations.size());

        // Extract unique party IDs to fetch full names
        Set<String> uniquePartyIds = simulations.stream()
                .map(Simulation::getPartyId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Resolve party names efficiently in a single query
        Map<String, Party> partyMap = partyPortRepository.findPartyNames(uniquePartyIds);

        // Assign party to each simulation
        simulations.forEach(simulation -> {
            simulation.setParty(partyMap.get(simulation.getPartyId()));
        });

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
            throw new IllegalArgumentException(INVALID_SIMULATION_ID_MESSAGE);
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
            throw new IllegalArgumentException(INVALID_SIMULATION_ID_MESSAGE);
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
            throw new IllegalArgumentException(INVALID_SIMULATION_ID_MESSAGE);
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

        // Generate scenario name if not provided
        if (dto.getScenarioName() == null || dto.getScenarioName().isBlank()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String defaultName = "Simulación " + formatter.format(new Date());
            dto.setScenarioName(defaultName);
            log.debug(LogMessage.SERVICE_SIMULATION_DEFAULT_NAME, defaultName);
        }

        // Check if a simulation with this scenario name already exists for this request
        if (simulationPortRepository.existsByRequestIdAndScenarioName(dto.getRequestId(), dto.getScenarioName())) {
            log.warn(LogMessage.SERVICE_SIMULATION_ALREADY_EXISTS, dto.getScenarioName(),
                    request.getId());
            throw new DuplicateSimulationNameException("Ya existe una simulación con este nombre.");
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
        if (dto.getSimulatedResults() != null) {
            FinancialMetrics fm = null;
            if (dto.getSimulatedResults().getMonthlyPayment() != null
                    || dto.getSimulatedResults().getDti() != null
                    || dto.getSimulatedResults().getTotalPayment() != null
                    || dto.getSimulatedResults().getTotalInterest() != null
                    || dto.getSimulatedResults().getDisposableIncome() != null) {
                fm = new FinancialMetrics(
                        dto.getSimulatedResults().getMonthlyPayment(),
                        dto.getSimulatedResults().getDti(),
                        dto.getSimulatedResults().getTotalPayment(),
                        dto.getSimulatedResults().getTotalInterest(),
                        dto.getSimulatedResults().getDisposableIncome());
            }
            RiskMetrics simulatedResults = new RiskMetrics(
                    dto.getSimulatedResults().getPd(),
                    dto.getSimulatedResults().getLgd(),
                    dto.getSimulatedResults().getEad(),
                    dto.getSimulatedResults().getEcl(),
                    dto.getSimulatedResults().getRiskGrade(),
                    fm);
            simulation.setSimulatedResults(simulatedResults);
            simulation.setSimulatedDecision(dto.getSimulatedResults().getDecision());
        }

        // Deltas
        if (dto.getDelta() != null) {
            simulation.setPdChange(dto.getDelta().getPdChange());
            simulation.setEclChange(dto.getDelta().getEclChange());
            simulation.setRiskGradeChange(dto.getDelta().getRiskGradeChange());
            simulation.setMonthlyPaymentChange(dto.getDelta().getMonthlyPaymentChange());
            simulation.setDtiChange(dto.getDelta().getDtiChange());
            simulation.setTotalPaymentChange(dto.getDelta().getTotalPaymentChange());
            simulation.setTotalInterestChange(dto.getDelta().getTotalInterestChange());
            simulation.setMonthlyDisposableIncomeChange(dto.getDelta().getMonthlyDisposableIncomeChange());
        }
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

        // Simulated results and decision
        if (dto.getSimulatedResults() != null) {
            FinancialMetrics fm = null;
            if (dto.getSimulatedResults().getMonthlyPayment() != null
                    || dto.getSimulatedResults().getDti() != null
                    || dto.getSimulatedResults().getTotalPayment() != null
                    || dto.getSimulatedResults().getTotalInterest() != null
                    || dto.getSimulatedResults().getDisposableIncome() != null) {
                fm = new FinancialMetrics(
                        dto.getSimulatedResults().getMonthlyPayment(),
                        dto.getSimulatedResults().getDti(),
                        dto.getSimulatedResults().getTotalPayment(),
                        dto.getSimulatedResults().getTotalInterest(),
                        dto.getSimulatedResults().getDisposableIncome());
            }
            RiskMetrics simulatedResults = new RiskMetrics(
                    dto.getSimulatedResults().getPd(),
                    dto.getSimulatedResults().getLgd(),
                    dto.getSimulatedResults().getEad(),
                    dto.getSimulatedResults().getEcl(),
                    dto.getSimulatedResults().getRiskGrade(),
                    fm);
            simulation.setSimulatedResults(simulatedResults);
            simulation.setSimulatedDecision(dto.getSimulatedResults().getDecision());
        }

        // Deltas
        if (dto.getDelta() != null) {
            simulation.setPdChange(dto.getDelta().getPdChange());
            simulation.setEclChange(dto.getDelta().getEclChange());
            simulation.setRiskGradeChange(dto.getDelta().getRiskGradeChange());
            simulation.setMonthlyPaymentChange(dto.getDelta().getMonthlyPaymentChange());
            simulation.setDtiChange(dto.getDelta().getDtiChange());
            simulation.setTotalPaymentChange(dto.getDelta().getTotalPaymentChange());
            simulation.setTotalInterestChange(dto.getDelta().getTotalInterestChange());
            simulation.setMonthlyDisposableIncomeChange(dto.getDelta().getMonthlyDisposableIncomeChange());
        }

        simulation.setArchived(false);

        return simulation;
    }
}
