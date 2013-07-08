package com.church.elim.domain;

import com.church.elim.builders.DomainBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 7/8/13
 * Time: 11:02 PM
 */
public class EntityTest extends Entity {
    public DomainBuilder getEntityBuilder() {
        return entityBuilder;
    }

    public void setEntityBuilder(DomainBuilder entityBuilder) {
        this.entityBuilder = entityBuilder;
    }

    private DomainBuilder entityBuilder;

    public EntityTest(String entityName) {
        super(entityName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        super.setApplicationContext(applicationContext);
        try {
            Class entityBuilderClass = Class.forName(entityClass.getName().replace("domain", "builder") + "Builder");
            this.entityBuilder = (DomainBuilder) ReflectionUtils.findMethod(entityBuilderClass, "a" + entityName).invoke(null, null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
