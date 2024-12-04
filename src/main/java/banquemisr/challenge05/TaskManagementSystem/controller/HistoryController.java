package banquemisr.challenge05.TaskManagementSystem.controller;

import banquemisr.challenge05.TaskManagementSystem.dto.HistoryQueryCriteriaRequest;
import banquemisr.challenge05.TaskManagementSystem.dto.ResponseModel;
import banquemisr.challenge05.TaskManagementSystem.model.History;
import banquemisr.challenge05.TaskManagementSystem.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/histories")
public class HistoryController {

    private final HistoryService historyService;

    /**
     * Endpoint to retrieve all histories with pagination and filtering.
     *
     * @param queryRequest Criteria to filter the histories.
     * @return A paginated response of histories with HATEOAS links.
     */
    @PostMapping
    public ResponseEntity<ResponseModel<List<History>>> filterHistories(@RequestBody HistoryQueryCriteriaRequest queryRequest) throws Exception {

        Pageable pageable = PageRequest.of(queryRequest.getPage(), queryRequest.getSize());

        // Get the filtered and paginated results from the service
        Page<History> historyPage = historyService.getHistories(queryRequest, pageable);

        // Pagination metadata
        ResponseModel.PaginationMetadata paginationMetadata = new ResponseModel.PaginationMetadata(
                historyPage.getSize(),
                historyPage.getTotalElements(),
                historyPage.getTotalPages(),
                historyPage.getNumber()
        );

        // Create and wrap the data into the ResponseModel
        ResponseModel<List<History>> response = new ResponseModel<>(historyPage.getContent(), paginationMetadata);

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to retrieve a single history entry by its ID.
     *
     * @param id The ID of the history entry.
     * @return The history entry with HATEOAS links.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<History>> getHistoryById(@PathVariable Long id) {
        History history = historyService.getHistoryById(id);
        return ResponseEntity.ok(new ResponseModel<>(history, null));
    }
}
