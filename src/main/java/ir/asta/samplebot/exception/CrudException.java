package ir.asta.samplebot.exception;

import ir.asta.samplebot.entities.core.BaseEntity;

public class CrudException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private BaseEntity baseEntity;

    public CrudException(Throwable cause, BaseEntity baseEntity) {
        super(cause);
        this.baseEntity = baseEntity;
    }

    public BaseEntity getDomainObject() {
        return baseEntity;
    }
}
