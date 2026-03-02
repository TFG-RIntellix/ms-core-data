package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;

/**
 * Mapper class to convert between Request (domain) and RequestDetailsDTO
 * (application).
 * This class will be responsible for mapping the Request entity from the domain
 * layer to the RequestDetails
 * DTO in the application layer, allowing to transfer detailed information about
 * a request without exposing the internal structure of the Request entity.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
@Component
public class RequestDetailsDTOMapper {

       public RequestDetailsDTO toDTO(Request request) {
              RequestDetailsDTO requestDetailsDTO = new RequestDetailsDTO();
              requestDetailsDTO.setRequestId(request.getId());
              requestDetailsDTO.setRequestDate(request.getCreationDate().toString());
              requestDetailsDTO.setRequestType(request.getRequestDetails().getRequestType().toString());
              requestDetailsDTO.setStatus(request.getRequestStatus().toString());
              requestDetailsDTO.setRequestedAmount(request.getRequestDetails().getRequestedAmount() != null
                            ? request.getRequestDetails().getRequestedAmount().getAmount()
                            : null);
              requestDetailsDTO.setCurrency(request.getRequestDetails().getRequestedAmount() != null
                            ? request.getRequestDetails().getRequestedAmount().getCurrency()
                            : null);
              requestDetailsDTO.setRequestTermMonths(request.getRequestDetails().getTermMonths());
              requestDetailsDTO.setInterestRate(request.getRequestDetails().getInterestRate());
              requestDetailsDTO.setPurpose(request.getRequestDetails().getPurpose().toString());
              // Map party fields
              requestDetailsDTO.setPartyName(request.getParty().getPersonDetails().getFullName());
              requestDetailsDTO.setPartyNIF(request.getParty().getPersonDetails().getNif());
              requestDetailsDTO.setPartyPhoneNumber(request.getParty().getPersonDetails().getContactInfo().getPhoneNumber());
              requestDetailsDTO.setPartyEmail(request.getParty().getPersonDetails().getContactInfo().getEmail());
              requestDetailsDTO.setPartyAddress(request.getParty().getPersonDetails().getContactInfo().getAddress());
              requestDetailsDTO.setPartyLaboralSituation(request.getParty().getPersonDetails().getFinancials().getEmploymentStatus().toString());
              requestDetailsDTO.setPartyIncome(request.getParty().getPersonDetails().getFinancials().getAnnualIncome().toString());
              return requestDetailsDTO;
       }

}
