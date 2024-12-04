package banquemisr.challenge05.TaskManagementSystem.repo;


import banquemisr.challenge05.TaskManagementSystem.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepo  extends JpaRepository<History, Long> , JpaSpecificationExecutor<History> {
}