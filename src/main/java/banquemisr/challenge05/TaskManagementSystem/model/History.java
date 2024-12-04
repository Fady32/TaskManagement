package banquemisr.challenge05.TaskManagementSystem.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
@Getter
@Setter
@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "old_task_status")
    private String oldTaskStatus;

    @Column(name = "new_task_status")
    private String newTaskStatus;

    @Column(name = "old_task_priority")
    private String oldTaskPriority;

    @Column(name = "new_task_priority")
    private String newTaskPriority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime timestamp;
}
