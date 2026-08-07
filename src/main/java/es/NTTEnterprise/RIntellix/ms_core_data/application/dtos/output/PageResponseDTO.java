package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output;

import java.util.List;

/**
 * Data Transfer Object for paginated API responses.
 * Encapsulates the list of results for the current page along with pagination metadata.
 * 
 * @param <T> the type of elements in this page
 * 
 * @author Lucía Fernández Mancebo
 * @date 31/07/2026
 */
public class PageResponseDTO<T> {

    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;

    public PageResponseDTO() {
    }

    public PageResponseDTO(List<T> content, long totalElements, int totalPages, int number, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;
        this.size = size;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
