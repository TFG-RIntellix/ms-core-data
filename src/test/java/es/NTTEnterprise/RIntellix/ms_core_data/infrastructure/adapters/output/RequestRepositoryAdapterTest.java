package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output;

import static org.junit.jupiter.api.Assertions.*;
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

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.RequestEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers.RequestMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository.RequestRepository;

import java.util.Date;
import java.time.ZoneId;

@DisplayName("RequestRepositoryAdapter Tests")
@ExtendWith(MockitoExtension.class)
class RequestRepositoryAdapterTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    private RequestRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new RequestRepositoryAdapter(requestRepository, requestMapper);
    }

    @Test
    @DisplayName("findRequestIdsBySearch should call repository and map object ids to hex strings")
    void testFindRequestIdsBySearch() {
        RequestEntity entity = new RequestEntity();
        entity.setId(new ObjectId());

        when(requestRepository.findRequestIdsBySearch("term")).thenReturn(List.of(entity));

        List<String> result = adapter.findRequestIdsBySearch("term");

        assertEquals(1, result.size());
        assertEquals(entity.getId().toHexString(), result.get(0));
        verify(requestRepository).findRequestIdsBySearch("term");
    }

    @Test
    @DisplayName("findRequestIdsBySearch should default to empty string when search is null")
    void testFindRequestIdsBySearch_NullSearch() {
        when(requestRepository.findRequestIdsBySearch("")).thenReturn(List.of());

        List<String> result = adapter.findRequestIdsBySearch(null);

        assertTrue(result.isEmpty());
        verify(requestRepository).findRequestIdsBySearch("");
    }

    @Test
    @DisplayName("updateReviewStatus should update status and date and save")
    void testUpdateReviewStatus_success() throws Exception {
        ObjectId id = new ObjectId();
        RequestEntity entity = new RequestEntity();
        entity.setId(id);

        when(requestRepository.findById(id)).thenReturn(Optional.of(entity));

        Date date = new Date();
        adapter.updateReviewStatus(id.toHexString(), RequestStatus.REVISADO, date);

        assertEquals(RequestStatus.REVISADO, entity.getStatus());
        assertEquals(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), entity.getLastReviewDate());
        verify(requestRepository).save(entity);
    }

    @Test
    @DisplayName("updateReviewStatus should throw EntityNotFoundException if request does not exist")
    void testUpdateReviewStatus_notFound() {
        ObjectId id = new ObjectId();

        when(requestRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> adapter.updateReviewStatus(id.toHexString(), RequestStatus.REVISADO, new Date()));
    }

    @Test
    @DisplayName("updateReviewStatus should only update status when date is null")
    void testUpdateReviewStatus_nullDate() throws Exception {
        ObjectId id = new ObjectId();
        RequestEntity entity = new RequestEntity();
        entity.setId(id);

        when(requestRepository.findById(id)).thenReturn(Optional.of(entity));

        adapter.updateReviewStatus(id.toHexString(), RequestStatus.REVISADO, null);

        assertEquals(RequestStatus.REVISADO, entity.getStatus());
        assertNull(entity.getLastReviewDate());
        verify(requestRepository).save(entity);
    }
}
