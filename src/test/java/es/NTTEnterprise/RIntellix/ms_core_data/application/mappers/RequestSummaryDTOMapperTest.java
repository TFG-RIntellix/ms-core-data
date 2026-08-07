package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Money;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Person;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RequestDetails;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;

class RequestSummaryDTOMapperTest {

    private final RequestSummaryDTOMapper mapper = new RequestSummaryDTOMapper();

    @Test
    @DisplayName("Should map Request to RequestSummaryDTO successfully")
    void toDTO_success() {
        Request request = new Request();
        request.setId("REQ-1");
        request.setRequestCode("CODE-1");
        request.setRequestStatus(RequestStatus.APROBADO);
        request.setCreationDate(new Date());
        request.setLastReviewDate(new Date());

        RequestDetails details = new RequestDetails(RequestType.HIPOTECA, Purpose.REFORMA_HOGAR, new Money(100000.0, "EUR"), 12, 5.0, null, false, "FIXED", "Personal");
        request.setRequestDetails(details);

        Party party = new Party();
        Person pd = new Person();
        pd.setFirstName("Alice");
        pd.setLastName("Smith");
        party.setPersonDetails(pd);
        request.setParty(party);

        RequestSummaryDTO dto = mapper.toDTO(request);

        assertEquals("REQ-1", dto.getRequestId());
        assertEquals("CODE-1", dto.getRequestCode());
        assertEquals("APROBADO", dto.getStatus());
        assertEquals("HIPOTECA", dto.getRequestType());
        assertEquals(100000.0, dto.getAmount());
        assertEquals("EUR", dto.getCurrency());
        assertNotNull(dto.getCreationDate());
        assertNotNull(dto.getLastReviewDate());
        assertEquals("Alice Smith", dto.getPartyName());
    }

    @Test
    @DisplayName("Should map list of Requests to list of RequestSummaryDTOs")
    void toDTOList_success() {
        Request request = new Request();
        request.setId("REQ-1");
        request.setRequestStatus(RequestStatus.PENDIENTE_DE_REVISION);
        request.setCreationDate(new Date());
        RequestDetails details = new RequestDetails(RequestType.TARJETA_CREDITO, Purpose.OTROS, new Money(0.0, "EUR"), 0, 0.0, new Money(5000.0, "EUR"), false, "FIXED", "Personal");
        request.setRequestDetails(details);
        Party party = new Party();
        Person pd = new Person();
        pd.setFirstName("Bob");
        party.setPersonDetails(pd);
        request.setParty(party);

        List<RequestSummaryDTO> dtos = mapper.toDTOList(List.of(request));

        assertEquals(1, dtos.size());
        assertEquals("REQ-1", dtos.get(0).getRequestId());
        assertEquals(0.0, dtos.get(0).getAmount());
    }
}
