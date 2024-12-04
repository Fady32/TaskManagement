package banquemisr.challenge05.TaskManagementSystem.repo;

import banquemisr.challenge05.TaskManagementSystem.dto.HistoryQueryCriteriaRequest;
import banquemisr.challenge05.TaskManagementSystem.model.History;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistorySpecification {

    /**
     * Build a specification for filtering history based on the criteria request.
     *
     * @param queryRequest The filtering criteria.
     * @return The specification to be used in the repository query.
     */
    public static Specification<History> buildHistorySpecification(HistoryQueryCriteriaRequest queryRequest) {
        List<Specification<History>> specs = new ArrayList<>();

        if (Objects.nonNull(queryRequest) && queryRequest.getAction() != null) {
            specs.add((root, query, builder) -> builder.equal(root.get("action"), queryRequest.getAction()));
        }

        if (Objects.nonNull(queryRequest) && queryRequest.getTaskId() != null) {
            specs.add((root, query, builder) -> builder.equal(root.get("task").get("id"), queryRequest.getTaskId()));
        }

        if (Objects.nonNull(queryRequest) && queryRequest.getUserId() != null) {
            specs.add((root, query, builder) -> builder.equal(root.get("user").get("id"), queryRequest.getUserId()));
        }

        if (Objects.nonNull(queryRequest) && queryRequest.getUsername() != null) {
            specs.add((root, query, builder) -> builder.equal(root.get("user").get("username"), queryRequest.getUsername()));
        }


        // Combine all specifications using 'and'

        return specs.stream()
                .reduce(Specification::and)
                .orElse(null);
    }
}
