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
