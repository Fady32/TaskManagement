package banquemisr.challenge05.TaskManagementSystem.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class TaskSearchResponseDto {

    private List<TaskResponseDto> tasks;
    private Pagination pagination;
    private Links links;

    // Nested classes for pagination and links
    public static class Pagination {
        private int size;
        private long totalElements;
        private int totalPages;
        private int number;

        // getters and setters
    }

    public static class Links {
        private String self;
        private String next;
        private String prev;

        // getters and setters
    }
}