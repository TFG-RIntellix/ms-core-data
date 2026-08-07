package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Money;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;

/**
 * Mapper class to convert between Request (domain) and RequestSummaryDTO
 * (application).
 * 
 * @author Lucía Fernández Mancebo
 * @date 28/02/2026
 */
public class RequestSummaryDTOMapper {

        /**
         * Maps a Request domain object to a RequestSummaryDTO.
         * 
         * @param request the Request domain object to map
         * @return the mapped RequestSummaryDTO
         */
        public RequestSummaryDTO toDTO(Request request) {
                RequestSummaryDTO requestSummaryDTO = new RequestSummaryDTO();
                requestSummaryDTO.setRequestId(request.getId());
                requestSummaryDTO.setRequestCode(request.getRequestCode());
                requestSummaryDTO.setStatus(request.getRequestStatus().toString());
                requestSummaryDTO.setRequestType(request.getRequestDetails().getRequestType().toString());
                Money money = request.getRequestDetails().getRequestedAmount() != null
                                ? request.getRequestDetails().getRequestedAmount()
                                : request.getRequestDetails().getCreditLimit();

                requestSummaryDTO.setAmount(money != null ? money.getAmount() : null);
                requestSummaryDTO.setCurrency(money != null ? money.getCurrency() : null);
                requestSummaryDTO.setCreationDate(request.getCreationDate().toInstant().toString());
                requestSummaryDTO
                                .setLastReviewDate(request.getLastReviewDate() != null
                                                ? request.getLastReviewDate().toInstant().toString()
                                                : null);
                requestSummaryDTO.setPartyName(request.getParty().getPersonDetails().getFullName());

                return requestSummaryDTO;
        }

        /**
         * Maps a list of Request domain objects to RequestSummaryDTO list.
         * 
         * @param requests list of Request domain objects
         * @return list of mapped RequestSummaryDTO objects
         */
        public List<RequestSummaryDTO> toDTOList(List<Request> requests) {
                return requests.stream()
                                .map(this::toDTO)
                                .toList();
        }
}
