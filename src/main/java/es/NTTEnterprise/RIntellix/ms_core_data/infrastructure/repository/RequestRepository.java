package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.RequestEntity;

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
     * Finds requests with dynamic filtering based on a generic search term (ID or party name) and request status.
     * The search term applies to the _id (regex) OR party_id (in list of IDs).
     * 
     * @param search   the search term to match against _id (optional)
     * @param partyIds the list of party IDs to match against (optional)
     * @param status   the status of the request (optional filter)
     * @return list of request entities matching the provided filters
     */
    @Query("{ $and: [ " +
            "{ $or: [ " +
                "{ $expr: { $eq: [:#{#search}, ''] } }, " +
                "{ $expr: { $regexMatch: { input: { $toString: '$_id' }, regex: :#{#search}, options: 'i' } } }, " +
                "{ 'party_id': { $in: :#{#partyIds} } } " +
            "] }, " +
            "{ $or: [ { $expr: { $eq: [:#{#status}, null] } }, { 'status': :#{#status} } ] } " +
            "] }")
    List<RequestEntity> findWithFilters(@Param("search") String search, @Param("partyIds") List<ObjectId> partyIds, @Param("status") String status);

}
