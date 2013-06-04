package com.church.elim.controller.rest;

import com.church.elim.service.DomainService;
import com.church.elim.utils.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 6/2/13
 * Time: 12:10 AM
 */
@Controller
public class RestDomainController<E> implements ApplicationContextAware {
    ApplicationContext ctx;
    DomainService<E> domainService;
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<E> get(@PathVariable Long id) {
        return new ResponseEntity<E>(domainService.get(id), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<E> get(@RequestBody E entity) {

        return new ResponseEntity<E>(domainService.add(entity), HttpStatus.OK);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
        String entityName = ReflectionUtils.getActualTypeOfGenericParameter(this, 0).getSimpleName().toLowerCase();
        this.domainService = (DomainService<E>) this.ctx.getBean(entityName + "Service");
    }
}
