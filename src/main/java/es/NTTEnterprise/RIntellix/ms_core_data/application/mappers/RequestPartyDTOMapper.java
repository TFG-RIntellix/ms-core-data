package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestPartyDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;

/**
 * Mapper that converts a {@link Request} domain object and a {@link Party} 
 * domain object into the lightweight {@link RequestPartyDTO}, exposing only 
 * the party identifiers (id and name) associated with the request.
 *
 * @author Lucía Fernández Mancebo
 * @date 27/08/2026
 */
public class RequestPartyDTOMapper {

    /**
     * Maps a Request domain object and a partial Party domain object to a RequestPartyDTO.
     *
     * @param request the Request domain object containing the request and party IDs
     * @param party   the Party domain object containing the resolved party name
     * @return the mapped RequestPartyDTO
     */
    public RequestPartyDTO toDTO(Request request, Party party) {
        RequestPartyDTO requestPartyDTO = new RequestPartyDTO();
        requestPartyDTO.setRequestId(request.getId());
        requestPartyDTO.setPartyId(request.getPartyId());
        requestPartyDTO.setPartyName(party != null && party.getPersonDetails() != null
                ? party.getPersonDetails().getFullName()
                : null);
        return requestPartyDTO;
    }

}
