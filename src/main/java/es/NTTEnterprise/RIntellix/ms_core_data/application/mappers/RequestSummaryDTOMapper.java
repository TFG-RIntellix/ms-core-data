package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;

/**
 * Mapper class to convert between Request (domain) and RequestSummaryDTO (application).
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
@Component
public class RequestSummaryDTOMapper {

    // I want to map all the Request entities in domain into the RequestSummaryDTO, so I need to create a method that takes a Request entity as input and returns a RequestSummaryDTO as output.
    
    public RequestSummaryDTO toDTO(Request request) {
        RequestSummaryDTO requestSummaryDTO = new RequestSummaryDTO();
        requestSummaryDTO.setStatus(request.getRequestStatus().toString());
        requestSummaryDTO.setRequestType(request.getRequestDetails().getRequestType().toString());
        requestSummaryDTO.setAmount(request.getRequestDetails().getRequestedAmount() != null ? request.getRequestDetails().getRequestedAmount().getAmount() : null);
        requestSummaryDTO.setCurrency(request.getRequestDetails().getRequestedAmount() != null ? request.getRequestDetails().getRequestedAmount().getCurrency() : null);
        requestSummaryDTO.setCreationDate(request.getCreationDate().toString());
        requestSummaryDTO.setLastReviewDate(request.getLastReviewDate() != null ? request.getLastReviewDate().toString() : null);
        return requestSummaryDTO;
    }

    public List<RequestSummaryDTO> toDTOList(List<Request> requests) {
        return requests.stream()
                .map(this::toDTO)
                .toList();
    }
}
