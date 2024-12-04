package banquemisr.challenge05.TaskManagementSystem.repo;

import banquemisr.challenge05.TaskManagementSystem.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo  extends JpaRepository<Task, Long> , JpaSpecificationExecutor<Task> {

    @EntityGraph(attributePaths = "users")
    List<Task> findAll(Specification<Task> specification);

    List<Task> findAll();
}
