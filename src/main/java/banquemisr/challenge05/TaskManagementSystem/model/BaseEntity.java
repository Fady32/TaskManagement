package banquemisr.challenge05.TaskManagementSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * This is a base entity class that provides common fields for auditing purposes,
 * including the creation and update timestamps.
 * <p>
 * This class is meant to be extended by other entities to automatically handle
 * the creation and modification date management.
 * </p>
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * The timestamp when the entity was created.
     * This value is set only once when the entity is first persisted.
     */
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    /**
     * The timestamp when the entity was last updated.
     * This value is automatically updated each time the entity is modified.
     */
    private LocalDateTime updatedDate;

    /**
     * Callback method triggered before the entity is persisted to set the created date.
     * This method is automatically called by JPA during the persist lifecycle.
     */
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    /**
     * Callback method triggered before the entity is updated to set the updated date.
     * This method is automatically called by JPA during the update lifecycle.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
