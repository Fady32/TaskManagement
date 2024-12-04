package banquemisr.challenge05.TaskManagementSystem.service;

import banquemisr.challenge05.TaskManagementSystem.dto.HistoryQueryCriteriaRequest;
import banquemisr.challenge05.TaskManagementSystem.model.History;
import banquemisr.challenge05.TaskManagementSystem.model.Task;
import banquemisr.challenge05.TaskManagementSystem.model.User;
import banquemisr.challenge05.TaskManagementSystem.repo.HistoryRepo;
import banquemisr.challenge05.TaskManagementSystem.repo.HistorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HistoryService {


    private final HistoryRepo historyRepo;

    /**
     * Retrieve paginated and filtered histories.
     *
     * @param queryRequest Criteria to filter the histories.
     * @param pageable     Pageable for pagination.
     * @return A paginated list of histories.
     */
    public Page<History> getHistories(HistoryQueryCriteriaRequest queryRequest, Pageable pageable) throws Exception {

        if (pageable.getPageSize() < 0 || pageable.getPageSize() > 100) {
            throw new Exception("Page size must be from 1 to 100");
        }
        Specification<History> spec = HistorySpecification.buildHistorySpecification(queryRequest);
        return historyRepo.findAll(spec, pageable);
    }

    /**
     * Retrieve a single history entry by its ID.
     *
     * @param id The ID of the history entry.
     * @return The history entry.
     */
    public History getHistoryById(Long id) {
        return historyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("History not found"));
    }

    public void createHistory(Task task, Task oldTask, User user, String action) {
        History history = new History();

        // Set the action and task reference
        history.setAction(action);
        history.setTask(task);

        // Capture old and new status
        if (oldTask != null) {
            history.setOldTaskStatus(oldTask.getStatus());
            history.setOldTaskPriority(oldTask.getPriority());
        }
        history.setNewTaskStatus(task.getStatus());
        history.setNewTaskPriority(task.getPriority());

        // Set the timestamp
        history.setTimestamp(LocalDateTime.now());

        history.setUser(user);

        // Save the history object to the database
        historyRepo.save(history);
    }
}
