package banquemisr.challenge05.TaskManagementSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Represents a user in the task management system.
 * This entity contains the user's credentials, roles, and the tasks they are associated with.
 * <p>
 * The user has a unique username, password, email, and a set of roles, as well as a collection of tasks assigned to them.
 * </p>
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    /**
     * The unique identifier for the user.
     * It is automatically generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username of the user.
     * This field is required, unique, and cannot be null.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * The password for the user.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The email address of the user.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    private String email;

    /**
     * A set of roles assigned to the user.
     * Roles are stored as simple strings and are eagerly fetched.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;

    /**
     * A many-to-many relationship between users and tasks.
     * A user can be assigned to multiple tasks, and each task can have multiple users.
     * The association is maintained by the "users" field in the Task entity.
     * The cascade option ensures that changes to the user are propagated to related tasks.
     */
    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Task> tasks;
}