package es.NTTEnterprise.RIntellix.ms_core_data.utils;

/**
 * Centralized log messages for the ms-core-data microservice.
 * This class provides consistent and reusable log message templates
 * to ensure uniform logging across all layers of the application.
 * 
 * @author Lucía Fernández Mancebo
 * @date 01/03/2026
 */
public final class LogMessage {

    public static final String UTILITY_CLASS_NEVER_INSTANTIATE = "Never instantiate";

    private LogMessage() {
        throw new UnsupportedOperationException(UTILITY_CLASS_NEVER_INSTANTIATE);
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
    
    public static final String CONTROLLER_REPORT_NOT_FOUND_REQ = "Report not found for requestId: {}";
    public static final String CONTROLLER_REPORT_NO_FILE = "Report {} has no file path associated";
    public static final String CONTROLLER_REPORT_FILE_NOT_READABLE = "File {} for report {} does not exist or is not readable";
    public static final String CONTROLLER_REPORT_URL_ERROR = "Error generating URL for file path: {}";

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
    public static final String SERVICE_GET_DETAILS_TRIGGER_SCORING = "Triggering asynchronous scoring generation for request - requestId: [{}]";

    public static final String SERVICE_GET_PARTY_START = "Starting getRequestParty operation - requestId: [{}]";
    public static final String SERVICE_GET_PARTY_COMPLETE = "getRequestParty operation completed successfully - requestId: [{}]";
    
    public static final String SERVICE_MARK_REVIEWED_START = "Starting markRequestAsReviewed for requestId: [{}]";
    public static final String SERVICE_MARK_REVIEWED_SUCCESS = "Request status updated to REVISADO for requestId: [{}]";
    public static final String SERVICE_MARK_REVIEWED_SKIPPED = "Request status is not PENDIENTE_DE_REVISION for requestId: [{}], no update performed.";

    public static final String SERVICE_GET_SCORING_START = "Starting getScoringByRequestId operation - requestId: [{}]";
    public static final String SERVICE_GET_SCORING_VALIDATION = "Validating requestId for scoring: [{}]";
    public static final String SERVICE_GET_SCORING_VALIDATION_ERROR = "Validation failed for scoring requestId - Value is null or blank";
    public static final String SERVICE_GET_SCORING_FOUND = "Scoring found for request - requestId: [{}]";
    public static final String SERVICE_GET_SCORING_COMPLETE = "getScoringByRequestId operation completed successfully - requestId: [{}]";

    // Scoring Generation Service
    public static final String SERVICE_SCORING_GENERATION_START = "Starting scoring generation workflow - requestId: [{}]";
    public static final String SERVICE_SCORING_GENERATION_ALREADY_EXISTS = "Scoring already exists for request - requestId: [{}], skipping generation";
    public static final String SERVICE_SCORING_GENERATION_PUBLISHED = "Scoring generation request published for request - requestId: [{}]";
    public static final String SERVICE_SCORING_GENERATION_ENTITY_NOT_FOUND = "Entity not found during scoring generation - requestId: [{}], error: {}";
    public static final String SERVICE_SCORING_GENERATION_UNEXPECTED_ERROR = "Unexpected error during scoring generation - requestId: [{}], error: {}";


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

    // Simulation Create (POST)
    public static final String SERVICE_CREATE_SIMULATION_START = "Starting createSimulation operation - requestId: [{}], scenarioName: [{}]";
    public static final String SERVICE_CREATE_SIMULATION_PARTY_MISMATCH = "Party mismatch for simulation creation - requestId: [{}], provided partyId: [{}], actual partyId: [{}]";
    public static final String SERVICE_CREATE_SIMULATION_COMPLETE = "createSimulation operation completed successfully - simulationId: [{}]";

    // Simulation Archive (PATCH - Soft Delete)
    public static final String SERVICE_ARCHIVE_SIMULATION_START = "Starting archiveSimulation operation - simulationId: [{}]";
    public static final String SERVICE_ARCHIVE_SIMULATION_VALIDATION_ERROR = "Validation failed for simulationId - Value is null or blank";
    public static final String SERVICE_ARCHIVE_SIMULATION_FOUND = "Simulation found for archiving - simulationId: [{}]";
    public static final String SERVICE_ARCHIVE_SIMULATION_COMPLETE = "archiveSimulation operation completed successfully - simulationId: [{}], isArchived: [{}]";
    public static final String SERVICE_SIMULATION_DEFAULT_NAME = "No scenario name provided. Generated default name: {}";
    public static final String SERVICE_SIMULATION_ALREADY_EXISTS = "Simulation with name '{}' already exists for request {}";

    // Simulation Delete (DELETE)
    public static final String SERVICE_DELETE_SIMULATION_START = "Starting deleteSimulation operation - simulationId: [{}]";
    public static final String SERVICE_DELETE_SIMULATION_COMPLETE = "deleteSimulation operation completed successfully - simulationId: [{}]";

    // ============================================================
    // INFRASTRUCTURE LAYER - Repository Adapter
    // ============================================================

    public static final String REPOSITORY_FIND_ALL_START = "Executing findAll operation on database";
    public static final String REPOSITORY_FIND_ALL_RESULT = "findAll operation completed - Retrieved {} entity(ies)";

    public static final String REPOSITORY_FIND_BY_ID_START = "Executing findById operation - requestId: [{}]";
    public static final String REPOSITORY_FIND_BY_ID_FOUND = "Entity found in database - requestId: [{}]";
    public static final String REPOSITORY_FIND_BY_ID_NOT_FOUND = "Entity not found in database - requestId: [{}]";
    public static final String REPOSITORY_FIND_BY_ID_MAPPING = "Mapping RequestEntity to Request domain - requestId: [{}]";
    public static final String REPOSITORY_REQUEST_UPDATING_REVIEW_STATUS = "Updating review status for requestId: [{}]";

    public static final String REPOSITORY_FIND_WITH_FILTERS_START = "Executing findWithFilters operation - partyName: [{}], requestStatus: [{}]";
    public static final String REPOSITORY_FIND_WITH_FILTERS_RESULT = "findWithFilters operation completed - Retrieved {} entity(ies)";
    public static final String REPOSITORY_FIND_WITH_FILTERS_MAPPING = "Mapping {} RequestEntity(ies) to Request domain objects";

    // Party Repository
    public static final String REPOSITORY_PARTY_FIND_BY_ID_START = "Executing findById operation - partyId: [{}]";
    public static final String REPOSITORY_PARTY_FIND_BY_ID_FOUND = "Party found in database - partyId: [{}]";
    public static final String REPOSITORY_PARTY_FIND_BY_ID_NOT_FOUND = "Party not found in database - partyId: [{}]";
    public static final String REPOSITORY_PARTY_FIND_BY_ID_MAPPING = "Mapping PartyEntity to Party domain - partyId: [{}]";

    public static final String REPOSITORY_PARTY_FIND_NAME_ONLY_START = "Executing findPartyWithNameOnly operation - partyId: [{}]";
    public static final String REPOSITORY_PARTY_FIND_NAMES_ONLY_START = "REPOSITORY_PARTY_FIND_NAMES_ONLY_START: {}";
    public static final String REPOSITORY_PARTY_FIND_IDS_BY_NAME_START = "REPOSITORY_PARTY_FIND_IDS_BY_NAME_START: {}";
    public static final String REPOSITORY_PARTY_FIND_IDS_BY_NAME_RESULT = "REPOSITORY_PARTY_FIND_IDS_BY_NAME_RESULT: found {} parties";
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
    public static final String REPOSITORY_SCORING_SAVE_START = "Executing save operation on scorings - scoringId: [{}]";
    public static final String REPOSITORY_SCORING_SAVE_ENTITY_MAPPED = "ScoringEntity mapped for saving - entity: {}";
    public static final String REPOSITORY_SCORING_SAVE_COMPLETE = "Save operation on scorings completed - scoringId: [{}]";
    public static final String REPOSITORY_SCORING_SAVE_FAILED = "Save operation on scorings failed - error: {}";

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
    public static final String REPOSITORY_SIMULATION_DELETE_START = "Executing delete operation on simulations - simulationId: [{}]";
    public static final String REPOSITORY_SIMULATION_DELETE_NOT_FOUND = "Simulation not found for deletion - simulationId: [{}]";
    public static final String REPOSITORY_SIMULATION_DELETE_NOT_ARCHIVED = "Cannot delete simulation - simulation is not archived - simulationId: [{}]";
    public static final String REPOSITORY_SIMULATION_DELETE_COMPLETE = "Delete operation on simulations completed - simulationId: [{}]";
    public static final String REPOSITORY_SIMULATION_CHECK_EXISTS = "Checking if simulation exists with scenarioName: {} for requestId: {}";

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
    // KAFKA CONSUMER LAYER - ScoringKafkaConsumer
    // ============================================================

    public static final String KAFKA_CONSUMER_MESSAGE_RECEIVED = "[ScoringKafkaConsumer/Input] - MESSAGE_RECEIVED: from topic: {}, requestId: {}";
    public static final String LOG_VALUE_UNKNOWN = "UNKNOWN";

    public static final String KAFKA_CONSUMER_MESSAGE_PROCESSING_START = "[ScoringKafkaConsumer/Input] - PROCESSING_START: requestId: {}, messageContent: {}";

    public static final String KAFKA_CONSUMER_MESSAGE_PROCESSING_SUCCESS = "[ScoringKafkaConsumer/Input] - PROCESSING_SUCCESS: requestId: {}, scoringId: {}, message manually ACKed";

    public static final String KAFKA_CONSUMER_MESSAGE_VALIDATION_FAILED = "[ScoringKafkaConsumer/Input] - VALIDATION_FAILED: requestId: {}, error: {}";

    public static final String KAFKA_CONSUMER_MESSAGE_PERSISTENCE_FAILED = "[ScoringKafkaConsumer/Input] - PERSISTENCE_FAILED: requestId: {}, error: {}";

    public static final String KAFKA_CONSUMER_IDEMPOTENCY_CHECK = "[ScoringKafkaConsumer/Input] - IDEMPOTENCY_CHECK: requestId: {}, existingScoring: {}";
    public static final String KAFKA_CONSUMER_VALIDATION_REJECTED = "[ScoringKafkaConsumer/Input] - VALIDATION_REJECTED: offset: {}, validationErrors: {}";
    public static final String KAFKA_CONSUMER_VALIDATION_DETAIL = "[ScoringKafkaConsumer/Input] - VALIDATION_DETAIL: field: {}, message: {}";
    public static final String KAFKA_CONSUMER_ERROR = "[ScoringKafkaConsumer/Input] - ERROR: offset: {}, exception: {}";

    // Kafka Producer - Scoring Generation
    public static final String KAFKA_SCORING_GENERATION_PUBLISH_START = "[ScoringKafkaProducer/Output] - PUBLISH_START: requestId: {}";
    public static final String KAFKA_SCORING_GENERATION_PUBLISH_SUCCESS = "[ScoringKafkaProducer/Output] - PUBLISH_SUCCESS: topic: {}, requestId: {}";
    public static final String KAFKA_SCORING_GENERATION_PUBLISH_DTO = "[ScoringKafkaProducer/Output] - PUBLISH_PAYLOAD: {}";
    public static final String KAFKA_SCORING_GENERATION_PUBLISH_ERROR = "[ScoringKafkaProducer/Output] - PUBLISH_ERROR: requestId: {}, error: {}";
    public static final String KAFKA_SCORING_GENERATION_PUBLISH_STRATEGY = "[ScoringKafkaProducer/Output] - PUBLISH_STRATEGY: type: {}, requestId: {}";

    // ============================================================
    // SERVICE LAYER - ScoringConsumerService
    // ============================================================

    public static final String SERVICE_SCORING_CONSUMER_START = "[ScoringConsumerService/Application] - PROCESS_START: requestId: {}";

    public static final String SERVICE_SCORING_CONSUMER_VALIDATION = "[ScoringConsumerService/Application] - VALIDATION: requestId: {}, modelVersion: {}";

    public static final String SERVICE_SCORING_CONSUMER_IDEMPOTENT = "[ScoringConsumerService/Application] - IDEMPOTENT: Scoring already exists for requestId: {}, scoringId: {}";

    public static final String SERVICE_SCORING_CONSUMER_PERSIST = "[ScoringConsumerService/Application] - PERSIST: requestId: {}, riskGrade: {}, pd: {}, lgd: {}";

    public static final String SERVICE_SCORING_CONSUMER_COMPLETE = "[ScoringConsumerService/Application] - COMPLETE: requestId: {}, scoringId: {}, topFeaturesCount: {}";

    // ============================================================
    // REPORT LAYER - Service / Repository
    // ============================================================

    public static final String SERVICE_CREATE_REPORT_START = "Starting createReport operation - requestId: [{}], scoringId: [{}]";
    public static final String SERVICE_CREATE_REPORT_COMPLETE = "createReport operation completed successfully - reportId: [{}]";

    public static final String SERVICE_LIST_REPORTS_START = "Starting listReports operation";
    public static final String SERVICE_LIST_REPORTS_RESULT = "listReports operation completed - Found {} report(s)";
    
    public static final String SERVICE_GET_REPORT_REQ_START = "Starting getReportByRequestId operation - requestId: [{}]";
    public static final String SERVICE_GET_REPORT_REQ_COMPLETE = "getReportByRequestId operation completed";
    public static final String SERVICE_GET_REPORT_ID_START = "Starting getReport operation - reportId: [{}]";
    public static final String SERVICE_GET_REPORT_ID_FAILED = "getReport operation failed - reportId is null or empty";
    public static final String SERVICE_GET_REPORT_ID_COMPLETE = "getReport operation completed - Retrieved report: [{}]";

    public static final String REPOSITORY_REPORT_FIND_ALL_START = "Executing findAll operation on reports";
    public static final String REPOSITORY_REPORT_FIND_ALL_COMPLETE = "findAll operation on reports completed - Retrieved {} entity(ies)";

    public static final String REPOSITORY_REPORT_FIND_BY_REQUEST_ID_START = "Executing findByRequestId on reports - requestId: [{}]";
    public static final String REPOSITORY_REPORT_INVALID_OBJECT_ID = "Invalid ObjectId format for requestId: {}";
    public static final String REPOSITORY_REPORT_NOT_FOUND_FOR_REQUEST_ID = "No report found for requestId: {}";
    public static final String REPOSITORY_REPORT_FIND_BY_REQUEST_ID_COMPLETED = "findByRequestId on reports completed - Retrieved entity";
    public static final String REPOSITORY_REPORT_FIND_BY_ID_START = "Executing findById on reports - id: [{}]";
    public static final String REPOSITORY_REPORT_NOT_FOUND_FOR_ID = "Report not found for id: [{}]";
    public static final String REPOSITORY_REPORT_FIND_BY_ID_COMPLETED = "findById on reports completed - Retrieved entity";

    public static final String REPOSITORY_REPORT_SAVE_START = "Executing save operation on reports - requestId: [{}], scoringId: [{}]";
    public static final String REPOSITORY_REPORT_SAVE_COMPLETE = "Save operation on reports completed - reportId: [{}]";

    // ============================================================
    // EXCEPTION MESSAGES
    // ============================================================

    public static final String EXCEPTION_ENTITY_NOT_FOUND = "Entity not found - Type: {} - ID: [{}]";
    public static final String EXCEPTION_ILLEGAL_ARGUMENT = "Illegal argument: {}";
    public static final String EXCEPTION_UNEXPECTED = "Unexpected exception occurred: {}";
    public static final String API_ERROR_UNEXPECTED_MESSAGE = "An unexpected error occurred. Please try again later.";
    
    public static final String EXCEPTION_SIMULATION_NOT_FOUND = "Simulation with ID %s not found";
    public static final String EXCEPTION_REQUEST_NOT_FOUND = "Request with ID %s not found";
    public static final String EXCEPTION_PARTY_NOT_FOUND = "Party with ID %s not found";
    public static final String EXCEPTION_INVALID_REQUEST_ID = "Request ID cannot be null or empty";
    public static final String EXCEPTION_REQUEST_TYPE_NULL = "RequestType value cannot be null";
    public static final String EXCEPTION_REQUEST_TYPE_UNKNOWN = "Unknown RequestType value: %s";
    public static final String EXCEPTION_INVALID_REPORT_TYPE = "Invalid report type: %s";
    public static final String EXCEPTION_INVALID_SEVERITY = "Invalid risk factor severity: %s";
    public static final String EXCEPTION_MONEY_CURRENCY_MISMATCH = "Cannot add Money with different currencies";
    public static final String EXCEPTION_MAPPER_NEVER_INSTANTIATE = "Never instantiate";

    // ============================================================
    // APPLICATION LIFECYCLE
    // ============================================================

    public static final String APP_STARTING = "Starting ms-core-data microservice...";
    public static final String APP_STARTED = "ms-core-data microservice started successfully";
    public static final String APP_SHUTTING_DOWN = "Shutting down ms-core-data microservice...";
}
