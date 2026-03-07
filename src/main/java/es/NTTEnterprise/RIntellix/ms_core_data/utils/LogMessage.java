package es.NTTEnterprise.RIntellix.ms_core_data.utils;

/**
 * Centralized log messages for the ms-core-data microservice.
 * This class provides consistent and reusable log message templates
 * to ensure uniform logging across all layers of the application.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public final class LogMessage {

    private LogMessage() {
        // Private constructor to prevent instantiation
    }

    // ============================================================
    // CONTROLLER LAYER - Input Adapter
    // ============================================================

    public static final String CONTROLLER_REQUEST_RECEIVED = "Request received: {} {}";
    public static final String CONTROLLER_REQUEST_PARAMS = "Request parameters - partyName: [{}], requestStatus: [{}]";
    public static final String CONTROLLER_SIMULATION_REQUEST_PARAMS = "Request parameters - requestId: [{}], partyName: [{}], partyId: [{}]";
    public static final String CONTROLLER_REQUEST_PATH_VAR = "Path variable - requestId: [{}]";
    public static final String CONTROLLER_RESPONSE_SUCCESS = "Response sent successfully - Status: {} - Items count: {}";
    public static final String CONTROLLER_RESPONSE_SUCCESS_SINGLE = "Response sent successfully - Status: {} - RequestId: [{}]";
    public static final String CONTROLLER_RESPONSE_ERROR = "Error response sent - Status: {} - Reason: {}";
    public static final String CONTROLLER_VALIDATION_ERROR = "Validation error: {}";
    public static final String CONTROLLER_UNEXPECTED_ERROR = "Unexpected error processing request: {}";

    // ============================================================
    // APPLICATION LAYER - Use Cases / Services
    // ============================================================

    public static final String SERVICE_LIST_REQUESTS_START = "Starting listRequests operation with filters - partyName: [{}], requestStatus: [{}]";
    public static final String SERVICE_LIST_REQUESTS_RESULT = "listRequests operation completed - Found {} request(s)";
    public static final String SERVICE_LIST_REQUESTS_MAPPING = "Mapping {} request(s) to RequestSummaryDTO";

    public static final String SERVICE_GET_DETAILS_START = "Starting getRequestDetails operation - requestId: [{}]";
    public static final String SERVICE_GET_DETAILS_VALIDATION = "Validating requestId: [{}]";
    public static final String SERVICE_GET_DETAILS_VALIDATION_ERROR = "Validation failed for requestId - Value is null or blank";
    public static final String SERVICE_GET_DETAILS_FOUND = "Request found - requestId: [{}]";
    public static final String SERVICE_GET_DETAILS_MAPPING = "Mapping Request to RequestDetailsDTO - requestId: [{}]";
    public static final String SERVICE_GET_DETAILS_COMPLETE = "getRequestDetails operation completed successfully - requestId: [{}]";
    public static final String SERVICE_GET_DETAILS_RETRIEVE_PARTY = "Retrieving associated Party for request - partyId: [{}]";

    public static final String SERVICE_GET_SCORING_START = "Starting getScoringByRequestId operation - requestId: [{}]";
    public static final String SERVICE_GET_SCORING_VALIDATION = "Validating requestId for scoring: [{}]";
    public static final String SERVICE_GET_SCORING_VALIDATION_ERROR = "Validation failed for scoring requestId - Value is null or blank";
    public static final String SERVICE_GET_SCORING_FOUND = "Scoring found for request - requestId: [{}]";
    public static final String SERVICE_GET_SCORING_COMPLETE = "getScoringByRequestId operation completed successfully - requestId: [{}]";

    // Simulation Service
    public static final String SERVICE_LIST_SIMULATIONS_START = "Starting listSimulations operation with filters - requestId: [{}], partyName: [{}], partyId: [{}]";
    public static final String SERVICE_LIST_SIMULATIONS_RESULT = "listSimulations operation completed - Found {} simulation(s)";
    public static final String SERVICE_LIST_SIMULATIONS_MAPPING = "Mapping {} simulation(s) to SimulationSummaryDTO";
    public static final String SERVICE_LIST_SIMULATIONS_FILTERING_BY_NAME = "Applying post-fetch partyName filter: [{}]";
    public static final String SERVICE_LIST_SIMULATIONS_AFTER_FILTER = "After partyName filter - {} simulation(s) remaining";

    public static final String SERVICE_GET_SIMULATION_DETAILS_START = "Starting getSimulationDetails operation - simulationId: [{}]";
    public static final String SERVICE_GET_SIMULATION_DETAILS_VALIDATION = "Validating simulationId: [{}]";
    public static final String SERVICE_GET_SIMULATION_DETAILS_VALIDATION_ERROR = "Validation failed for simulationId - Value is null or blank";
    public static final String SERVICE_GET_SIMULATION_DETAILS_FOUND = "Simulation found - simulationId: [{}]";
    public static final String SERVICE_GET_SIMULATION_DETAILS_SCORING_FOUND = "Base scoring resolved for simulation comparison - scoringId: [{}]";
    public static final String SERVICE_GET_SIMULATION_DETAILS_SCORING_NOT_FOUND = "Base scoring not found for simulation comparison - scoringId: [{}]";
    public static final String SERVICE_GET_SIMULATION_DETAILS_COMPLETE = "getSimulationDetails operation completed successfully - simulationId: [{}]";

    // Simulation Update (PUT - Template)
    public static final String SERVICE_UPDATE_SIMULATION_TEMPLATE_START = "Starting updateSimulationTemplate operation - simulationId: [{}]";
    public static final String SERVICE_UPDATE_SIMULATION_TEMPLATE_VALIDATION_ERROR = "Validation failed for simulationId - Value is null or blank";
    public static final String SERVICE_UPDATE_SIMULATION_TEMPLATE_FOUND = "Simulation template found - simulationId: [{}]";
    public static final String SERVICE_UPDATE_SIMULATION_TEMPLATE_COMPLETE = "updateSimulationTemplate operation completed successfully - simulationId: [{}]";

    // Simulation Archive (PATCH - Soft Delete)
    public static final String SERVICE_ARCHIVE_SIMULATION_START = "Starting archiveSimulation operation - simulationId: [{}]";
    public static final String SERVICE_ARCHIVE_SIMULATION_VALIDATION_ERROR = "Validation failed for simulationId - Value is null or blank";
    public static final String SERVICE_ARCHIVE_SIMULATION_FOUND = "Simulation found for archiving - simulationId: [{}]";
    public static final String SERVICE_ARCHIVE_SIMULATION_COMPLETE = "archiveSimulation operation completed successfully - simulationId: [{}], isArchived: [{}]";

    // ============================================================
    // INFRASTRUCTURE LAYER - Repository Adapter
    // ============================================================

    public static final String REPOSITORY_FIND_ALL_START = "Executing findAll operation on database";
    public static final String REPOSITORY_FIND_ALL_RESULT = "findAll operation completed - Retrieved {} entity(ies)";

    public static final String REPOSITORY_FIND_BY_ID_START = "Executing findById operation - requestId: [{}]";
    public static final String REPOSITORY_FIND_BY_ID_FOUND = "Entity found in database - requestId: [{}]";
    public static final String REPOSITORY_FIND_BY_ID_NOT_FOUND = "Entity not found in database - requestId: [{}]";
    public static final String REPOSITORY_FIND_BY_ID_MAPPING = "Mapping RequestEntity to Request domain - requestId: [{}]";

    public static final String REPOSITORY_FIND_WITH_FILTERS_START = "Executing findWithFilters operation - partyName: [{}], requestStatus: [{}]";
    public static final String REPOSITORY_FIND_WITH_FILTERS_RESULT = "findWithFilters operation completed - Retrieved {} entity(ies)";
    public static final String REPOSITORY_FIND_WITH_FILTERS_MAPPING = "Mapping {} RequestEntity(ies) to Request domain objects";

    // Party Repository
    public static final String REPOSITORY_PARTY_FIND_BY_ID_START = "Executing findById operation - partyId: [{}]";
    public static final String REPOSITORY_PARTY_FIND_BY_ID_FOUND = "Party found in database - partyId: [{}]";
    public static final String REPOSITORY_PARTY_FIND_BY_ID_NOT_FOUND = "Party not found in database - partyId: [{}]";
    public static final String REPOSITORY_PARTY_FIND_BY_ID_MAPPING = "Mapping PartyEntity to Party domain - partyId: [{}]";

    public static final String REPOSITORY_PARTY_FIND_NAME_ONLY_START = "Executing findPartyWithNameOnly operation - partyId: [{}]";
    public static final String REPOSITORY_PARTY_FIND_NAME_ONLY_NOT_FOUND = "Party not found for name query - partyId: [{}]";
    public static final String REPOSITORY_PARTY_FIND_NAME_ONLY_RESULT = "Partial Party retrieved - partyId: [{}], fullName: [{}]";

    // Contract Repository
    public static final String REPOSITORY_CONTRACT_FIND_BY_PARTY_START = "Executing findByPartyId operation on contracts - partyId: [{}]";
    public static final String REPOSITORY_CONTRACT_FIND_BY_PARTY_RESULT = "findByPartyId operation completed - partyId: [{}], contracts found: {}";
    public static final String REPOSITORY_CONTRACT_FIND_ACTIVE_START = "Executing findActiveByPartyId operation on contracts - partyId: [{}]";
    public static final String REPOSITORY_CONTRACT_FIND_ACTIVE_RESULT = "findActiveByPartyId operation completed - partyId: [{}], active contracts found: {}";
    public static final String REPOSITORY_PARTY_LOADING_CONTRACTS = "Loading active contracts for Party aggregate - partyId: [{}]";
    public static final String REPOSITORY_PARTY_CONTRACTS_LOADED = "Active contracts loaded for Party aggregate - partyId: [{}], count: {}";

    // Scoring Repository
    public static final String REPOSITORY_SCORING_FIND_BY_REQUEST_START = "Executing findLatestByRequestId operation on scorings - requestId: [{}]";
    public static final String REPOSITORY_SCORING_FIND_BY_REQUEST_FOUND = "Scoring found for request - requestId: [{}]";
    public static final String REPOSITORY_SCORING_FIND_BY_REQUEST_NOT_FOUND = "Scoring not found for request - requestId: [{}]";
    public static final String REPOSITORY_SCORING_FIND_BY_ID_START = "Executing findById operation on scorings - scoringId: [{}]";
    public static final String REPOSITORY_SCORING_FIND_BY_ID_FOUND = "Scoring found in database - scoringId: [{}]";
    public static final String REPOSITORY_SCORING_FIND_BY_ID_NOT_FOUND = "Scoring not found in database - scoringId: [{}]";

    // Simulation Repository
    public static final String REPOSITORY_SIMULATION_FIND_ALL_START = "Executing findAll operation on simulations";
    public static final String REPOSITORY_SIMULATION_FIND_ALL_RESULT = "findAll operation on simulations completed - Retrieved {} entity(ies)";
    public static final String REPOSITORY_SIMULATION_FIND_BY_ID_START = "Executing findById operation on simulations - simulationId: [{}]";
    public static final String REPOSITORY_SIMULATION_FIND_BY_ID_FOUND = "Simulation found in database - simulationId: [{}]";
    public static final String REPOSITORY_SIMULATION_FIND_BY_ID_NOT_FOUND = "Simulation not found in database - simulationId: [{}]";
    public static final String REPOSITORY_SIMULATION_FIND_WITH_FILTERS_START = "Executing findWithFilters operation on simulations - requestId: [{}], partyId: [{}]";
    public static final String REPOSITORY_SIMULATION_FIND_WITH_FILTERS_RESULT = "findWithFilters operation on simulations completed - Retrieved {} entity(ies)";
    public static final String REPOSITORY_SIMULATION_SAVE_START = "Executing save operation on simulations - simulationId: [{}]";
    public static final String REPOSITORY_SIMULATION_SAVE_COMPLETE = "Save operation on simulations completed - simulationId: [{}]";

    // ============================================================
    // INFRASTRUCTURE LAYER - Mappers
    // ============================================================

    public static final String MAPPER_TO_DOMAIN_START = "Starting conversion from RequestEntity to Request domain";
    public static final String MAPPER_TO_DOMAIN_NULL = "Received null RequestEntity - returning null";
    public static final String MAPPER_TO_DOMAIN_COMPLETE = "Conversion to Request domain completed successfully";
    public static final String MAPPER_TO_DOMAIN_COLLATERAL = "Building PropertyCollateral for mortgage request";

    public static final String MAPPER_TO_DTO_START = "Starting conversion from Request to DTO";
    public static final String MAPPER_TO_DTO_NULL = "Received null Request - returning null";
    public static final String MAPPER_TO_DTO_COMPLETE = "Conversion to DTO completed successfully";

    // ============================================================
    // DOMAIN LAYER - Calculations
    // ============================================================

    public static final String DOMAIN_TOTAL_DEBT_RESULT = "Total outstanding debt calculated - result: {}";
    public static final String DOMAIN_TOTAL_MONTHLY_PAYMENT_RESULT = "Total monthly debt payment calculated - result: {}";
    public static final String DOMAIN_DTI_RESULT = "Global DTI calculated - totalMonthlyPayment: {}, grossMonthlyIncome: {}, DTI: {}";
    public static final String DOMAIN_DTI_NO_INCOME = "DTI calculation skipped - annual income not available or zero";
    public static final String DOMAIN_LTV_RESULT = "LTV calculated - outstandingBalance: {}, propertyValue: {}, LTV: {}";
    public static final String DOMAIN_LTV_NO_DATA = "LTV calculation skipped - property value or outstanding balance not available";

    // ============================================================
    // EXCEPTION MESSAGES
    // ============================================================

    public static final String EXCEPTION_ENTITY_NOT_FOUND = "Entity not found - Type: {} - ID: [{}]";
    public static final String EXCEPTION_ILLEGAL_ARGUMENT = "Illegal argument: {}";
    public static final String EXCEPTION_UNEXPECTED = "Unexpected exception occurred: {}";

    // ============================================================
    // APPLICATION LIFECYCLE
    // ============================================================

    public static final String APP_STARTING = "Starting ms-core-data microservice...";
    public static final String APP_STARTED = "ms-core-data microservice started successfully";
    public static final String APP_SHUTTING_DOWN = "Shutting down ms-core-data microservice...";
}
