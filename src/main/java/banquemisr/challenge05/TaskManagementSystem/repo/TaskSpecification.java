package banquemisr.challenge05.TaskManagementSystem.repo;

import banquemisr.challenge05.TaskManagementSystem.dto.TaskQueryCriteriaRequest;
import banquemisr.challenge05.TaskManagementSystem.model.Task;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskSpecification {

    /**
     * Build Specification for filtering tasks
     */
    public static Specification<Task> buildTaskSpecification(TaskQueryCriteriaRequest queryRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by title
            if (Objects.nonNull(queryRequest) && queryRequest.getTitle() != null) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + queryRequest.getTitle() + "%"));
            }

            // Filter by description
            if (Objects.nonNull(queryRequest) && queryRequest.getDescription() != null) {
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + queryRequest.getDescription() + "%"));
            }

            // Filter by status
            if (Objects.nonNull(queryRequest) && queryRequest.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), queryRequest.getStatus()));
            }

            // Filter by priority
            if (Objects.nonNull(queryRequest) && queryRequest.getDueDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("dueDate"), queryRequest.getDueDate()));
            }

            // Combine all predicates
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Task> buildTaskSpecificationForNotification(int notificationThresholdHours) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by due date (calculate the time difference and compare)
            if (notificationThresholdHours > 0) {
                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime thresholdTime = currentTime.plusHours(notificationThresholdHours);

                // Add condition to check if dueDate is within the threshold
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), currentTime));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), thresholdTime));

            }

            // Combine all predicates
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}