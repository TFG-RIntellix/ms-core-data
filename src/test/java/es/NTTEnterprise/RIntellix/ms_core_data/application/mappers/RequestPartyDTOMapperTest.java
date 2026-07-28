package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestPartyDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Person;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;

class RequestPartyDTOMapperTest {

    private final RequestPartyDTOMapper mapper = new RequestPartyDTOMapper();

    @Test
    @DisplayName("Should map Request and Party to RequestPartyDTO successfully")
    void toDTO_success() {
        Request request = new Request();
        request.setId("REQ-1");
        request.setPartyId("P-1");

        Party party = new Party();
        Person pd = new Person();
        pd.setFirstName("Jane");
        pd.setLastName("Doe");
        party.setPersonDetails(pd);

        RequestPartyDTO dto = mapper.toDTO(request, party);

        assertEquals("REQ-1", dto.getRequestId());
        assertEquals("P-1", dto.getPartyId());
        assertEquals("Jane Doe", dto.getPartyName());
    }

    @Test
    @DisplayName("Should handle null party or missing person details")
    void toDTO_nullParty() {
        Request request = new Request();
        request.setId("REQ-2");
        request.setPartyId("P-2");

        RequestPartyDTO dto1 = mapper.toDTO(request, null);
        assertNull(dto1.getPartyName());

        RequestPartyDTO dto2 = mapper.toDTO(request, new Party());
        assertNull(dto2.getPartyName());
    }
}
