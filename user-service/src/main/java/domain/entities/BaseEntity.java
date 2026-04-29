package domain.entities;

import java.sql.Timestamp;
import java.util.UUID;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseEntity extends PanacheEntityBase {
    @Id
    private UUID id;
    @Column(updatable = false)
    private Timestamp createdAt;
    @Column(updatable = false)
    private Timestamp updatedAt;

    @PrePersist
    protected void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        long now = System.currentTimeMillis();
        createdAt = new Timestamp(now);
        updatedAt = new Timestamp(now);
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
