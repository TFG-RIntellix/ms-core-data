package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.NotArchivedException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.SimulationPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.SimulationEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers.SimulationMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository.SimulationRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Infrastructure adapter that implements the SimulationPortRepository output
 * port.
 * Delegates persistence operations to the Spring Data SimulationRepository
 * and uses SimulationMapper to convert entities to domain objects.
 * Party resolution is handled at the application layer to respect SRP.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Slf4j
@Repository
public class SimulationRepositoryAdapter implements SimulationPortRepository {

    private final SimulationRepository simulationRepository;
    private final SimulationMapper simulationMapper;

    public SimulationRepositoryAdapter(SimulationRepository simulationRepository, SimulationMapper simulationMapper) {
        this.simulationRepository = Objects.requireNonNull(simulationRepository);
        this.simulationMapper = Objects.requireNonNull(simulationMapper);
    }

    @Override
    public Simulation findById(String simulationId) throws EntityNotFoundException, IllegalArgumentException {
        log.debug(LogMessage.REPOSITORY_SIMULATION_FIND_BY_ID_START, simulationId);

        Optional<SimulationEntity> entityOpt = simulationRepository.findById(new ObjectId(simulationId));

        if (entityOpt.isEmpty()) {
            log.warn(LogMessage.REPOSITORY_SIMULATION_FIND_BY_ID_NOT_FOUND, simulationId);
            throw new EntityNotFoundException("Simulation with ID " + simulationId + " not found");
        }

        log.debug(LogMessage.REPOSITORY_SIMULATION_FIND_BY_ID_FOUND, simulationId);
        return simulationMapper.toDomain(entityOpt.get());
    }

    @Override
    public List<Simulation> findWithFilters(String requestId, String partyId, boolean archived) {
        log.debug(LogMessage.REPOSITORY_SIMULATION_FIND_WITH_FILTERS_START, requestId, partyId);

        ObjectId requestOid = requestId != null ? new ObjectId(requestId) : null;
        ObjectId partyOid = partyId != null ? new ObjectId(partyId) : null;

        List<SimulationEntity> entities = simulationRepository.findWithFilters(requestOid, partyOid, archived);
        log.debug(LogMessage.REPOSITORY_SIMULATION_FIND_WITH_FILTERS_RESULT, entities.size());

        return entities.stream()
                .map(simulationMapper::toDomain)
                .toList();
    }

    @Override
    public Simulation save(Simulation simulation) {
        log.debug(LogMessage.REPOSITORY_SIMULATION_SAVE_START, simulation.getId());

        SimulationEntity entity = simulationMapper.toEntity(simulation);
        SimulationEntity savedEntity = simulationRepository.save(entity);

        log.debug(LogMessage.REPOSITORY_SIMULATION_SAVE_COMPLETE, savedEntity.getId());
        return simulationMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(String simulationId)
            throws IllegalArgumentException, EntityNotFoundException, NotArchivedException {
        log.debug(LogMessage.REPOSITORY_SIMULATION_DELETE_START, simulationId);

        ObjectId oid = new ObjectId(simulationId);

        Optional<SimulationEntity> entityOpt = simulationRepository.findById(oid);

        if (entityOpt.isEmpty()) {
            log.warn(LogMessage.REPOSITORY_SIMULATION_DELETE_NOT_FOUND, simulationId);
            throw new EntityNotFoundException("Simulation with ID " + simulationId + " not found");
        }

        SimulationEntity entity = entityOpt.get();
        if (!Boolean.TRUE.equals(entity.getIsArchived())) {
            log.warn(LogMessage.REPOSITORY_SIMULATION_DELETE_NOT_ARCHIVED, simulationId);
            throw new NotArchivedException(
                    "Cannot delete simulation that is not archived - simulationId: " + simulationId);
        }

        simulationRepository.deleteById(oid);
        log.debug(LogMessage.REPOSITORY_SIMULATION_DELETE_COMPLETE, simulationId);
    }
}
