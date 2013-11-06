package com.church.elim.domain;

import com.church.elim.web.controller.rest.RestDomainController;
import com.church.elim.service.DomainService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 7/8/13
 * Time: 10:34 PM
 */
public class Entity implements ApplicationContextAware {
    public static final String DOMAIN_PACKAGE = "com.church.elim.domain";
    @PersistenceContext
    private EntityManager em;
    protected String entityName;
    protected Class entityClass;
    private final String resourceUrl;
    private DomainService entityService;
    private RestDomainController entityController;

    public Entity(String entityName) {
        this.entityName = entityName;
        this.resourceUrl = "/" + entityName.toLowerCase() + "s";
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public DomainService getEntityService() {
        return entityService;
    }

    public void setEntityService(DomainService entityService) {
        this.entityService = entityService;
    }

    public RestDomainController getEntityController() {
        return entityController;
    }

    public void setEntityController(RestDomainController entityController) {
        this.entityController = entityController;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.entityService = (DomainService) applicationContext.getBean(entityName.toLowerCase() + "Service");
        this.entityController = (RestDomainController) applicationContext.getBean("rest" + entityName + "Controller");
        try {
            this.entityClass = Class.forName(DOMAIN_PACKAGE + "." + entityName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void mock(){

    }
}
