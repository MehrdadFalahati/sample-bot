package ir.asta.samplebot.service.core;

import java.io.Serializable;
import java.util.List;

public interface CrudService<T, U extends Serializable> {
    List<T> listAll();

    T getById(U id);

    T saveOrUpdate(T domainObject);

    void delete(U id);

    void delete(T domainObject);

    void nonLogicalDelete(U id);

    void deleteAll();
}
