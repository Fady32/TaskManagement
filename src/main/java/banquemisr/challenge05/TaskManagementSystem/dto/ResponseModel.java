package banquemisr.challenge05.TaskManagementSystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel<T> {

    private T data;  // Data to be returned
    private PaginationMetadata pagination;  // Pagination metadata

    // Pagination metadata class
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationMetadata {
        private int size;
        private long totalElements;
        private int totalPages;
        private int number;
    }

}
