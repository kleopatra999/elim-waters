package com.church.elim.service;

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
@Service
public class DomainService<E> implements ApplicationContextAware {
    JpaRepository<E, Long> repository;

    public DomainService() {

    }

    @Transactional
    public E add(E entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (!this.getClass().equals(DomainService.class)) {
            Class entityClass = getActualTypeOfGenericParameter(this, 0);
            String repositoryBeanName = entityClass.getSimpleName().toLowerCase() + "Repository";
            repository = (JpaRepository<E, Long>) applicationContext.getBean(repositoryBeanName);
        }
    }

    public E get(Long id) {
        return repository.findOne(id);
    }
}
