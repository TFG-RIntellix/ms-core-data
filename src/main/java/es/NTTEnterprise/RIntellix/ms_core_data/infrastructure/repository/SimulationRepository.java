package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.SimulationEntity;

/**
 * Repository interface for SimulationEntity in the "simulations" MongoDB
 * collection.
 * Extends Spring Data MongoDB's MongoRepository for standard CRUD operations
 * and defines a dynamic query for filtered retrieval of simulations.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public interface SimulationRepository extends MongoRepository<SimulationEntity, ObjectId> {

    /**
     * Finds simulations with dynamic filtering based on a generic search term, specific party IDs, and archive status.
     * The search term applies to the request_id (regex) OR party_id (in list of IDs).
     * 
     * @param search   the search term to match against request_id (optional)
     * @param partyIds the list of party IDs to match against (optional)
     * @param archived the archive status (optional filter)
     * @return list of simulation entities matching the provided filters
     */
    @Query("{ $and: [ " +
            "{ $or: [ " +
                "{ $expr: { $eq: [:#{#search}, ''] } }, " +
                "{ $expr: { $regexMatch: { input: { $toString: { $ifNull: [ '$request_id', '' ] } }, regex: :#{#search}, options: 'i' } } }, " +
                "{ 'party_id': { $in: :#{#partyIds} } } " +
            "] }, " +
            "{ $or: [ { $expr: { $eq: [:#{#archived}, null] } }, { 'is_archived': :#{#archived} } ] } " +
            "] }")
    List<SimulationEntity> findWithFilters(@Param("search") String search, @Param("partyIds") List<ObjectId> partyIds, @Param("archived") Boolean archived);

    /**
     * Checks if a simulation with the given scenario name already exists for the given request ID.
     * 
     * @param requestId the ID of the associated request
     * @param scenarioName the name of the scenario to check
     * @return true if a simulation exists, false otherwise
     */
    boolean existsByRequestIdAndScenarioName(ObjectId requestId, String scenarioName);

}
