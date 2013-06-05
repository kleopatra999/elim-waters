package com.church.elim.service;

import com.church.elim.domain.Identifiable;
import com.church.elim.repository.PersonRepository;
import com.church.elim.utils.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.church.elim.utils.ReflectionUtils.getActualTypeOfGenericParameter;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 6/1/13
 * Time: 5:10 PM
 */
public class DomainService<E extends Identifiable> implements ApplicationContextAware {
    JpaRepository<E, Long> repository;
    Class entityClass;
    String entityName;

    public DomainService() {
        this.entityClass = getActualTypeOfGenericParameter(this, 0);
        this.entityName = this.entityClass.getSimpleName();
    }

    @Transactional
    public E create(E entity) {
        return repository.saveAndFlush(entity);
    }

    @Transactional
    public E save(E entity) throws EntityDoesNotExistException {
        Long id = entity.getId();
        if(entity==null){
            throw new NullPointerException();
        }
        if(!exists(entity.getId())){
            throw new EntityDoesNotExistException(entityName, id);
        }

        return repository.saveAndFlush(entity);
    }

    public boolean exists(Long id){
        if(id == null
                || repository.findOne(id) == null){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (!this.getClass().equals(DomainService.class)) {
            String repositoryBeanName = entityClass.getSimpleName().toLowerCase() + "Repository";
            repository = (JpaRepository<E, Long>) applicationContext.getBean(repositoryBeanName);
        }
    }

    public E get(Long id) {
        return repository.findOne(id);
    }

    public void remove(Long id) throws EntityDoesNotExistException {
        if(!exists(id)){
            throw new EntityDoesNotExistException(entityName, id);
        }
        repository.delete(id);
    }
}
