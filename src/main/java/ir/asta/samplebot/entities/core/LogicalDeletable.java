package ir.asta.samplebot.entities.core;

import javax.persistence.PrePersist;

public interface LogicalDeletable {
    String DELETED_PROPERTY_NAME = "deleted";

    Boolean getDeleted();
    void setDeleted(Boolean deleted);

    class LogicalDeletableListener {

        @PrePersist
        public void onCreate(Object e ) {
            if (!(e instanceof LogicalDeletable)) {
                return;
            }
            LogicalDeletable entity = (LogicalDeletable) e;
            entity.setDeleted(false);
        }

    }
}
