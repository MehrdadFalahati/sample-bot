package ir.asta.samplebot.entities.user;

import ir.asta.samplebot.entities.core.AbstractBaseEntity;
import ir.asta.samplebot.entities.core.LogicalDeletable;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "BOT_USER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends AbstractBaseEntity implements LogicalDeletable {

    private String firstName;
    private String lastName;
    private Boolean isBot;
    private String username;
    private String languageCode;
    private Boolean deleted;

    @Override
    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
