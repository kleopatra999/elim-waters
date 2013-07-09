package com.church.elim.controller.rest;

import com.church.elim.domain.Identifiable;
import com.church.elim.service.DomainService;
import com.church.elim.service.EntityDoesNotExistException;
import com.church.elim.utils.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 6/2/13
 * Time: 12:10 AM
 */
public class RestDomainController<E extends Identifiable> implements ApplicationContextAware {
    ApplicationContext ctx;
    DomainService<E> domainService;
    Class controllerClass = this.getClass();
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<E> get(@PathVariable Long id) {
        System.out.println("The id is:" + id);
        return new ResponseEntity<E>(domainService.get(id), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> remove(@PathVariable Long id) throws EntityDoesNotExistException {
        domainService.remove(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<E> create(@RequestBody E entity) {
        E result = domainService.create(entity);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(controllerClass).slash(result.getId()).toUri());
        return new ResponseEntity<E>(result, headers, HttpStatus.CREATED);
    }

    /**
     * Merge entity with persisted entity with given id.
     *
     * @param entity
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<E> save(@RequestBody E entity, @PathVariable Long id) throws InvocationTargetException, IllegalAccessException, EntityDoesNotExistException {
        BeanUtils.setProperty(entity, "id", id);
        return new ResponseEntity<E>(domainService.save(entity), HttpStatus.OK);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
        String entityName = ReflectionUtils.getActualTypeOfGenericParameter(this, 0).getSimpleName().toLowerCase();
        this.domainService = (DomainService<E>) this.ctx.getBean(entityName + "Service");
    }
}
