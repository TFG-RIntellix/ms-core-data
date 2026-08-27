package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.PartyEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers.PartyMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.projections.PartyNameProjection;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository.PartyRepository;

@ExtendWith(MockitoExtension.class)
class PartyRepositoryAdapterTest {

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private PartyMapper partyMapper;

    private PartyRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PartyRepositoryAdapter(partyRepository, partyMapper);
    }

    @Test
    @DisplayName("Should find Party by ID successfully")
    void findById_success() throws EntityNotFoundException {
        String idStr = new ObjectId().toHexString();
        PartyEntity entity = new PartyEntity();
        Party domain = new Party();
        
        when(partyRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(entity));
        when(partyMapper.toDomain(entity)).thenReturn(domain);

        Party result = adapter.findById(idStr);

        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when Party by ID not found")
    void findById_notFound() {
        String idStr = new ObjectId().toHexString();
        when(partyRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adapter.findById(idStr));
    }

    @Test
    @DisplayName("Should find PartyName by ID successfully")
    void findPartyName_success() {
        String idStr = new ObjectId().toHexString();
        PartyNameProjection projection = mock(PartyNameProjection.class);
        Party domain = new Party();
        
        when(partyRepository.findPartyNameProjectionById(any(ObjectId.class))).thenReturn(projection);
        when(partyMapper.toPartialDomain(projection)).thenReturn(domain);

        Party result = adapter.findPartyName(idStr);

        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    @DisplayName("Should return null when PartyName by ID not found")
    void findPartyName_notFound() {
        String idStr = new ObjectId().toHexString();
        when(partyRepository.findPartyNameProjectionById(any(ObjectId.class))).thenReturn(null);

        assertNull(adapter.findPartyName(idStr));
    }

    @Test
    @DisplayName("Should find PartyNames by IDs successfully")
    void findPartyNames_success() {
        String idStr = new ObjectId().toHexString();
        Set<String> ids = Set.of(idStr);
        PartyNameProjection projection = mock(PartyNameProjection.class);
        Party domain = new Party();
        
        when(projection.getId()).thenReturn(idStr);
        when(partyRepository.findPartyNameProjectionsByIdIn(any())).thenReturn(List.of(projection));
        when(partyMapper.toPartialDomain(projection)).thenReturn(domain);

        Map<String, Party> result = adapter.findPartyNames(ids);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(domain, result.get(idStr));
    }

    @Test
    @DisplayName("Should return empty map when PartyNames by IDs receives null or empty")
    void findPartyNames_empty() {
        assertTrue(adapter.findPartyNames(null).isEmpty());
        assertTrue(adapter.findPartyNames(Set.of()).isEmpty());
    }

    @Test
    @DisplayName("Should find Party IDs by Name Match successfully")
    void findPartyIdsByNameMatch_success() {
        String idStr = new ObjectId().toHexString();
        PartyNameProjection projection = mock(PartyNameProjection.class);
        when(projection.getId()).thenReturn(idStr);
        
        when(partyRepository.findPartyNameProjectionsByNameMatch(anyString())).thenReturn(List.of(projection));

        Set<String> result = adapter.findPartyIdsByNameMatch("test");

        assertNotNull(result);
        assertTrue(result.contains(idStr));
    }

    @Test
    @DisplayName("Should return empty set when Party IDs by Name Match receives null or empty")
    void findPartyIdsByNameMatch_empty() {
        assertTrue(adapter.findPartyIdsByNameMatch(null).isEmpty());
        assertTrue(adapter.findPartyIdsByNameMatch("").isEmpty());
    }
}
