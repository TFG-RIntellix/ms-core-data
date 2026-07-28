package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ContactInfo;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialProfile;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Money;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Person;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RequestDetails;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.EmploymentStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;

class RequestDetailsDTOMapperTest {

    private final RequestDetailsDTOMapper mapper = new RequestDetailsDTOMapper();

    @Test
    @DisplayName("Should map Request to RequestDetailsDTO successfully")
    void toDTO_success() {
        Request request = new Request();
        request.setId("REQ-1");
        request.setCreationDate(new Date());
        request.setRequestStatus(RequestStatus.PENDIENTE_DE_REVISION);
        request.setLastReviewDate(new Date());

        RequestDetails details = new RequestDetails(RequestType.PRESTAMO, Purpose.COMPRA_VEHICULO, new Money(15000.0, "EUR"), 24, 5.5, null, false, "FIXED", "Personal");
        request.setRequestDetails(details);

        Party party = new Party();
        Person pd = new Person();
        pd.setFirstName("John");
        pd.setLastName("Doe");
        pd.setNif("12345678Z");
        ContactInfo contact = new ContactInfo();
        contact.setPhoneNumber("555-1234");
        contact.setEmail("john@example.com");
        contact.setAddress("123 Main St");
        pd.setContactInfo(contact);
        FinancialProfile fin = new FinancialProfile();
        fin.setEmploymentStatus(EmploymentStatus.INDEFINIDO);
        fin.setAnnualIncome(new Money(50000.0, "EUR"));
        pd.setFinancials(fin);
        party.setPersonDetails(pd);
        request.setParty(party);

        RequestDetailsDTO dto = mapper.toDTO(request);

        assertEquals("REQ-1", dto.getRequestId());
        assertNotNull(dto.getRequestDate());
        assertEquals("PRESTAMO", dto.getRequestType());
        assertEquals("PENDIENTE_DE_REVISION", dto.getStatus());
        assertEquals(15000.0, dto.getRequestedAmount());
        assertEquals(24, dto.getRequestTermMonths());
        assertEquals(5.5, dto.getInterestRate());
        assertEquals("COMPRA_VEHICULO", dto.getPurpose());
        assertEquals("John Doe", dto.getPartyName());
        assertEquals("12345678Z", dto.getPartyNIF());
        assertEquals("555-1234", dto.getPartyPhoneNumber());
        assertEquals("john@example.com", dto.getPartyEmail());
        assertEquals("123 Main St", dto.getPartyAddress());
        assertEquals("INDEFINIDO", dto.getPartyLaboralSituation());
        assertEquals(50000.0, dto.getPartyIncome());
        assertNull(dto.getRequestedCreditLimit());
        assertFalse(dto.getIsRevolving());
        assertEquals("EUR", dto.getCurrency());
        assertNotNull(dto.getLastReviewDate());
    }
}
