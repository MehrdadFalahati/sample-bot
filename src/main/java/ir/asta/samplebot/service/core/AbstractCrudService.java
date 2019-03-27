package ir.asta.samplebot.service.core;

import ir.asta.samplebot.entities.core.BaseEntity;
import ir.asta.samplebot.entities.core.LogicalDeletable;
import ir.asta.samplebot.exception.CrudException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AbstractCrudService<T , U extends Serializable> implements CrudService<T, U> {
    protected Class<T> entityClass;
    private CrudRepository<T, U> crudRepository;

    protected CrudRepository<T, U> getCrudRepository() {
        return crudRepository;
    }

    public void setCrudRepository(CrudRepository<T, U> crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> listAll() {
        log.debug("List of " + getEntityClass());
        List<T> list = new ArrayList<>();
        crudRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public T getById(U id) {
        Optional<T> entity = crudRepository.findById(id);
        if (entity.isPresent()) {
            log.debug("Find " + getEntityClass() + "[id:{}]", id);
            return entity.get();
        }
        log.error("Can not find Entity[id]:{}", id);
        throw new ObjectRetrievalFailureException(getEntityClass(), id);
    }

    @Override
    @Transactional
    public T saveOrUpdate(T domainObject) {
        try {
            Assert.notNull(domainObject);
            T save = crudRepository.save(domainObject);
            log.debug("Save/Update Entity:{} is success");
            return save;
        } catch (Exception e) {
            log.error("does not Save/Update entity by message exception: {}", e.getMessage());
            throw new CrudException(e, (BaseEntity) domainObject);
        }
    }

    @Override
    @Transactional
    public void delete(U id) {
        Assert.notNull(id);
        T entity = getById(id);
        delete(entity);
        log.debug("Entity[id]:{} is success to delete", id);
    }

    @Override
    @Transactional
    public void delete(T domainObject) {
        Assert.notNull(domainObject);
        if (domainObject instanceof LogicalDeletable) {
            logicalDelete(domainObject);
        } else {
            crudRepository.delete(domainObject);
        }
    }

    @Override
    @Transactional
    public void nonLogicalDelete(U id) {
        T entity = getById(id);
        if (entity instanceof LogicalDeletable) {
            crudRepository.delete(entity);
        } else {
            throw new UnsupportedOperationException("Entity is not logically deletable. because class "
                    + getEntityClass() + " does not implement interface " + LogicalDeletable.class.getName());
        }
    }

    @Override
    @Transactional
    public void deleteAll() {
        crudRepository.deleteAll();
    }

    private void logicalDelete(T domainObject) {
        LogicalDeletable logicalDeletable = (LogicalDeletable) domainObject;
        logicalDeletable.setDeleted(true);
        crudRepository.save(domainObject);
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntityClass() {
        if (entityClass == null) {
            Type type = getClass().getGenericSuperclass();
            while (type.equals(AbstractCrudService.class)) {
                type = getClass().getGenericSuperclass();
            }
            ParameterizedType paramType = (ParameterizedType) type;
            entityClass = (Class<T>) paramType.getActualTypeArguments()[0];
        }
        return entityClass;
    }
}
