package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.RequestEntity;

/**
 * Interface that serves as a repository for the Request aggregate, allowing to
 * perform CRUD operations on the database.
 * It extends the MongoRepository interface, which provides basic methods for
 * working with MongoDB, and it also defines custom queries to find requests by
 * party name and status.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
public interface RequestRepository extends MongoRepository<RequestEntity, ObjectId> {

    /**
     * Finds requests with dynamic filtering based on party name and request status.
     * Only non-null parameters are applied as filters. If a parameter is null, it
     * will not be used as a filter.
     * 
     * @param partyId the name of the party associated with the request (optional
     *                filter)
     * @param status  the status of the request (optional filter)
     * @return list of request entities matching the provided filters
     */
    @Query("{ $and: [ " +
            "{ $or: [ { $expr: { $eq: [:#{#partyId}, null] } }, { 'party.name': :#{#partyId} } ] }, " +
            "{ $or: [ { $expr: { $eq: [:#{#status}, null] } }, { 'status': :#{#status} } ] } " +
            "] }")
    List<RequestEntity> findWithFilters(@Param("partyId") String partyId, @Param("status") String status);

}
