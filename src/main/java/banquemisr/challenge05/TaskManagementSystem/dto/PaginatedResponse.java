package banquemisr.challenge05.TaskManagementSystem.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedResponse<T> {

    private List<T> content; // Data for the current page
    private int page; // Current page number
    private int size; // Page size
    private long totalElements; // Total number of elements
    private int totalPages; // Total number of pages
    private boolean last; // Is this the last page?
    private boolean first; // Is this the first page?
    private String nextPage; // URL for the next page
    private String prevPage; // URL for the previous page

}
