package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.util.List;

/**
 * Encapsulates a paginated result from the repository layer.
 * 
 * @param <T> the type of the entities in the page
 * 
 * @author Lucía Fernández Mancebo
 * @date 31/07/2026
 */
public class PagedResult<T> {

    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int number;
    private final int size;

    public PagedResult(List<T> content, long totalElements, int totalPages, int number, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;
        this.size = size;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }
}
