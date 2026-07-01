package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestPartyDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;

/**
 * Mapper that converts a {@link Request} domain object into the lightweight
 * {@link RequestPartyDTO}, exposing only the party identifiers (id and name)
 * associated with the request.
 *
 * @author Lucía Fernández Mancebo
 */
public class RequestPartyDTOMapper {

    /**
     * Maps a Request domain object to a RequestPartyDTO.
     *
     * @param request the Request domain object to map
     * @return the mapped RequestPartyDTO
     */
    public RequestPartyDTO toDTO(Request request) {
        RequestPartyDTO requestPartyDTO = new RequestPartyDTO();
        requestPartyDTO.setRequestId(request.getId());
        requestPartyDTO.setPartyId(request.getParty() != null && request.getParty().getId() != null
                ? request.getParty().getId()
                : request.getPartyId());
        requestPartyDTO.setPartyName(request.getParty() != null
                ? request.getParty().getPersonDetails().getFullName()
                : null);
        return requestPartyDTO;
    }

}
