package com.church.elim.domain;

import com.church.elim.builders.DomainBuilder;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 7/8/13
 * Time: 11:02 PM
 */
@Component
@Scope("prototype")
public class EntityHelper extends Entity implements ApplicationContextAware{
    private Identifiable aEntity;

    public DomainBuilder getEntityBuilder() {
        return entityBuilder;
    }

    public void setEntityBuilder(DomainBuilder entityBuilder) {
        this.entityBuilder = entityBuilder;
    }

    private DomainBuilder entityBuilder;

    public EntityHelper(String entityName) {
        super(entityName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        super.setApplicationContext(applicationContext);
        try {
            Class entityBuilderClass = Class.forName(entityClass.getName().replace("domain", "builders") + "Builder");
            this.entityBuilder = (DomainBuilder) ReflectionUtils.findMethod(entityBuilderClass, "a" + entityName).invoke(null, null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void mock(){
        setEntityService(Mockito.mock(getEntityService().getClass()));
        ReflectionTestUtils.setField(getEntityController(), "domainService", getEntityService());
        when(getEntityService().get(aEntity().getId())).thenReturn(aEntity());
    }

    public Identifiable aEntity() {
        if(this.aEntity == null){
            this.aEntity = (Identifiable) entityBuilder.build();
        }
        return aEntity;
    }
}
