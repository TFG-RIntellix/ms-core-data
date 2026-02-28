package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.RequestPortService;

/**
 * This class implements the RequestPortService interface and provides the implementation of the methods defined in the interface. 
 * It is responsible for handling the business logic related to requests, such as listing requests and getting request details.
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
public class RequestApplicationService implements RequestPortService {

    @Override
    public List<RequestSummaryDTO> listRequests(String partyName, String requestType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listRequests'");
    }

    @Override
    public RequestDetailsDTO getRequestDetails(String requestId)
            throws IllegalArgumentException, EntityNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRequestDetails'");
    }


}
