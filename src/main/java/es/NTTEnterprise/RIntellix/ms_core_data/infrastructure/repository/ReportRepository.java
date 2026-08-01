package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.ReportEntity;

/**
 * Repository interface for ReportEntity in the "reports" MongoDB collection.
 * Extends Spring Data MongoDB's MongoRepository for standard CRUD operations.
 *
 * @author Lucía Fernández Mancebo
 * @date 29/03/2026
 */
public interface ReportRepository extends MongoRepository<ReportEntity, ObjectId> {

    /**
     * Finds a single report by request ID.
     * 
     * @param requestId the ID to check against request_id
     * @return an Optional containing the matching report entity, or empty
     */
    Optional<ReportEntity> findFirstByRequestIdOrderByGeneratedDateDesc(ObjectId requestId);

    /**
     * Retrieves report aggregates from the database with dynamic filtering and
     * pagination.
     *
     * @param search     the generic search term (optional filter)
     * @param requestIds the list of request IDs to filter by (optional)
     * @param pageable   the pagination information
     * @return a Page of Report domain entities
     */
    @Query("{ $and: [ " +
            "{ $or: [ " +
            "{ $expr: { $eq: [:#{#search}, ''] } }, " +
            "{ $expr: { $regexMatch: { input: { $ifNull: [ '$title', '' ] }, regex: :#{#search}, options: 'i' } } }, " +
            "{ 'request_id': { $in: :#{#requestIds} } } " +
            "] } " +
            "] }")
    Page<ReportEntity> findWithFilters(
            @Param("search") String search,
            @Param("requestIds") List<ObjectId> requestIds,
            Pageable pageable);
}
