package banquemisr.challenge05.TaskManagementSystem.repo;


import banquemisr.challenge05.TaskManagementSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo  extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByUsernameIn(Set<String> usernames);
}