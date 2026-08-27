package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.ScoringEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers.ScoringMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository.ScoringRepository;

@ExtendWith(MockitoExtension.class)
class ScoringRepositoryAdapterTest {

    @Mock
    private ScoringRepository scoringRepository;

    @Mock
    private ScoringMapper scoringMapper;

    private ScoringRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ScoringRepositoryAdapter(scoringRepository, scoringMapper);
    }

    @Test
    @DisplayName("Should find Scoring by Request ID successfully")
    void findByRequestId_success() throws EntityNotFoundException {
        String idStr = new ObjectId().toHexString();
        ScoringEntity entity = new ScoringEntity();
        Scoring domain = new Scoring();
        
        when(scoringRepository.findFirstByRequestIdOrderByScoringDateDesc(any(ObjectId.class))).thenReturn(Optional.of(entity));
        when(scoringMapper.toDomain(entity)).thenReturn(domain);

        Scoring result = adapter.findByRequestId(idStr);

        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when Scoring by Request ID not found")
    void findByRequestId_notFound() {
        String idStr = new ObjectId().toHexString();
        when(scoringRepository.findFirstByRequestIdOrderByScoringDateDesc(any(ObjectId.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adapter.findByRequestId(idStr));
    }

    @Test
    @DisplayName("Should find Scoring by ID successfully")
    void findById_success() throws EntityNotFoundException {
        String idStr = new ObjectId().toHexString();
        ScoringEntity entity = new ScoringEntity();
        Scoring domain = new Scoring();
        
        when(scoringRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(entity));
        when(scoringMapper.toDomain(entity)).thenReturn(domain);

        Scoring result = adapter.findById(idStr);

        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when Scoring by ID not found")
    void findById_notFound() {
        String idStr = new ObjectId().toHexString();
        when(scoringRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adapter.findById(idStr));
    }

    @Test
    @DisplayName("Should save Scoring successfully")
    void save_success() {
        Scoring domain = new Scoring();
        domain.setId(new ObjectId().toHexString());
        ScoringEntity entity = new ScoringEntity();
        
        when(scoringMapper.toEntity(domain)).thenReturn(entity);
        when(scoringRepository.save(entity)).thenReturn(entity);
        when(scoringMapper.toDomain(entity)).thenReturn(domain);

        Scoring result = adapter.save(domain);

        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    @DisplayName("Should throw RuntimeException when save fails")
    void save_fails() {
        Scoring domain = new Scoring();
        domain.setId(new ObjectId().toHexString());
        
        when(scoringMapper.toEntity(domain)).thenThrow(new RuntimeException("DB Error"));

        assertThrows(RuntimeException.class, () -> adapter.save(domain));
    }
}
