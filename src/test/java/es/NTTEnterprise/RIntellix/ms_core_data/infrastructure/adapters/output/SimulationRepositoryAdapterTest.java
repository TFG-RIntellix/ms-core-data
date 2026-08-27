package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.PagedResult;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.NotArchivedException;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.SimulationEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers.SimulationMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository.SimulationRepository;

@ExtendWith(MockitoExtension.class)
class SimulationRepositoryAdapterTest {

    @Mock
    private SimulationRepository simulationRepository;

    @Mock
    private SimulationMapper simulationMapper;

    private SimulationRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SimulationRepositoryAdapter(simulationRepository, simulationMapper);
    }

    @Test
    @DisplayName("Should find Simulation by ID successfully")
    void findById_success() throws EntityNotFoundException {
        String idStr = new ObjectId().toHexString();
        SimulationEntity entity = new SimulationEntity();
        Simulation domain = new Simulation();

        when(simulationRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(entity));
        when(simulationMapper.toDomain(entity)).thenReturn(domain);

        Simulation result = adapter.findById(idStr);

        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when Simulation by ID not found")
    void findById_notFound() {
        String idStr = new ObjectId().toHexString();
        when(simulationRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adapter.findById(idStr));
    }

    @Test
    @DisplayName("Should find Simulations with filters successfully")
    void findWithFilters_success() {
        SimulationEntity entity = new SimulationEntity();
        Simulation domain = new Simulation();
        Page<SimulationEntity> page = new PageImpl<>(List.of(entity));

        when(simulationRepository.findWithFilters(anyString(), anyList(), anyList(), anyBoolean(), any(Pageable.class)))
                .thenReturn(page);
        when(simulationMapper.toDomain(entity)).thenReturn(domain);

        PagedResult<Simulation> result = adapter.findWithFilters("test", List.of(new ObjectId().toHexString()),
                List.of(), true, 0, 10, "id", "asc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getTotalElements());
    }

    @Test
    @DisplayName("Should save Simulation successfully")
    void save_success() {
        Simulation domain = new Simulation();
        domain.setId(new ObjectId().toHexString());
        SimulationEntity entity = new SimulationEntity();

        when(simulationMapper.toEntity(domain)).thenReturn(entity);
        when(simulationRepository.save(entity)).thenReturn(entity);
        when(simulationMapper.toDomain(entity)).thenReturn(domain);

        Simulation result = adapter.save(domain);

        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    @DisplayName("Should delete Simulation successfully")
    void delete_success() throws Exception {
        String idStr = new ObjectId().toHexString();
        SimulationEntity entity = new SimulationEntity();
        entity.setIsArchived(true);

        when(simulationRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> adapter.delete(idStr));
        verify(simulationRepository).deleteById(any(ObjectId.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException on delete when Simulation not found")
    void delete_notFound() {
        String idStr = new ObjectId().toHexString();
        when(simulationRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adapter.delete(idStr));
    }

    @Test
    @DisplayName("Should throw NotArchivedException on delete when Simulation is not archived")
    void delete_notArchived() {
        String idStr = new ObjectId().toHexString();
        SimulationEntity entity = new SimulationEntity();
        entity.setIsArchived(false); // not archived

        when(simulationRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(entity));

        assertThrows(NotArchivedException.class, () -> adapter.delete(idStr));
    }

    @Test
    @DisplayName("Should check if Simulation exists by Request ID and Scenario Name")
    void existsByRequestIdAndScenarioName_success() {
        when(simulationRepository.existsByRequestIdAndScenarioName(any(ObjectId.class), anyString())).thenReturn(true);

        assertTrue(adapter.existsByRequestIdAndScenarioName(new ObjectId().toHexString(), "Scenario 1"));
    }

    @Test
    @DisplayName("Should return false when check exists with null params")
    void existsByRequestIdAndScenarioName_nullParams() {
        assertFalse(adapter.existsByRequestIdAndScenarioName(null, "Scenario 1"));
        assertFalse(adapter.existsByRequestIdAndScenarioName(new ObjectId().toHexString(), null));
    }
}
