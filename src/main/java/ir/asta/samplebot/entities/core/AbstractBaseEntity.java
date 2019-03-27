package ir.asta.samplebot.entities.core;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
@EntityListeners({LogicalDeletable.LogicalDeletableListener.class})
public class AbstractBaseEntity implements BaseEntity {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Version
    private Integer version;

    private Date creationTime;
    private Date lastModificationTime;

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        lastModificationTime = new Date();
        if (creationTime == null) {
            creationTime = new Date();
        }
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
