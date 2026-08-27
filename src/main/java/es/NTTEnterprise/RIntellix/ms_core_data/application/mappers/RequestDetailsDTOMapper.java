package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;



import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Money;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;

/**
 * Mapper class to convert between Request (domain) and RequestDetailsDTO
 * (application).
 * 
 * Maps detailed request information including party and financial details
 * without exposing internal domain structure.
 * 
 * @author Lucía Fernández Mancebo
 * @date 28/02/2026
 */
public class RequestDetailsDTOMapper {

       /**
        * Maps a Request domain object to a RequestDetailsDTO.
        *
        * @param request the Request domain object to map
        * @return the mapped RequestDetailsDTO
        */
       public RequestDetailsDTO toDTO(Request request) {
              RequestDetailsDTO requestDetailsDTO = new RequestDetailsDTO();
              requestDetailsDTO.setRequestId(request.getId());
              requestDetailsDTO.setRequestCode(request.getRequestCode());
              requestDetailsDTO.setRequestDate(request.getCreationDate().toInstant().toString());
              requestDetailsDTO.setRequestType(request.getRequestDetails().getRequestType().toString());
              requestDetailsDTO.setStatus(request.getRequestStatus().toString());

              requestDetailsDTO.setLoanAmount(request.getRequestDetails().getRequestedAmount() != null
                            ? request.getRequestDetails().getRequestedAmount().getAmount()
                            : null);
              requestDetailsDTO.setRequestTermMonths(request.getRequestDetails().getTermMonths());
              requestDetailsDTO.setInterestRate(request.getRequestDetails().getInterestRate());
              requestDetailsDTO.setPurpose(request.getRequestDetails().getPurpose().toString());

              // Map party fields
              requestDetailsDTO.setPartyName(request.getParty().getPersonDetails().getFullName());
              requestDetailsDTO.setPartyNIF(request.getParty().getPersonDetails().getNif());
              requestDetailsDTO.setPartyPhoneNumber(
                            request.getParty().getPersonDetails().getContactInfo().getPhoneNumber());
              requestDetailsDTO.setPartyEmail(request.getParty().getPersonDetails().getContactInfo().getEmail());
              requestDetailsDTO.setPartyAddress(request.getParty().getPersonDetails().getContactInfo().getAddress());
              requestDetailsDTO.setPartyLaboralSituation(
                            request.getParty().getPersonDetails().getFinancials().getEmploymentStatus().toString());
              requestDetailsDTO.setPartyIncome(
                            request.getParty().getPersonDetails().getFinancials().getAnnualIncome().getAmount());

              // Map creditCard fields
              requestDetailsDTO.setCreditLimit(request.getRequestDetails().getCreditLimit() != null
                            ? request.getRequestDetails().getCreditLimit().getAmount()
                            : null);
              requestDetailsDTO.setIsRevolving(request.getRequestDetails().isRevolving());

              // Map common fields
              if (request.getCollateral() != null) {
                     requestDetailsDTO.setPropertyValue(request.getCollateral().getPropertyValue().getAmount());
                     requestDetailsDTO.setIsFirstHome(request.getCollateral().isFirstHome());
              }

              Money money = request.getRequestDetails().getRequestedAmount() != null
                            ? request.getRequestDetails().getRequestedAmount()
                            : request.getRequestDetails().getCreditLimit();
              requestDetailsDTO.setCurrency(money != null ? money.getCurrency() : null);
              requestDetailsDTO.setAmount(money != null ? money.getAmount() : null);
              requestDetailsDTO.setCreationDate(request.getCreationDate().toInstant().toString());
              requestDetailsDTO.setLastReviewDate(request.getLastReviewDate() != null
                            ? request.getLastReviewDate().toInstant().toString()
                            : null);

              return requestDetailsDTO;
       }

}
