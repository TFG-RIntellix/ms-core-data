package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.SimulationEntity;

/**
 * Repository interface for SimulationEntity in the "simulations" MongoDB
 * collection.
 * Extends Spring Data MongoDB's MongoRepository for standard CRUD operations
 * and defines a dynamic query for filtered retrieval of simulations.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public interface SimulationRepository extends MongoRepository<SimulationEntity, String> {

    /**
     * Finds simulations with dynamic filtering based on request ID and party ID.
     * Only non-null parameters are applied as filters. If a parameter is null, it
     * will not be used as a filter.
     * When both parameters are null, all simulations are returned.
     * 
     * @param requestId the ID of the associated request (optional filter, as
     *                  ObjectId)
     * @param partyId   the ID of the associated party (optional filter, as
     *                  ObjectId)
     * @return list of simulation entities matching the provided filters
     */
    @Query("{ $and: [ " +
            "{ $or: [ { $expr: { $eq: [:#{#requestId}, null] } }, { 'request_id': :#{#requestId} } ] }, " +
            "{ $or: [ { $expr: { $eq: [:#{#partyId}, null] } }, { 'party_id': :#{#partyId} } ] } " +
            "] }")
    List<SimulationEntity> findWithFilters(@Param("requestId") ObjectId requestId, @Param("partyId") ObjectId partyId);

}
