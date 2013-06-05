package com.church.elim.service;

import com.church.elim.ElimTest;
import com.church.elim.domain.Caregiver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 6/1/13
 * Time: 5:20 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class DomainServiceTest extends ElimTest{
    @Autowired
    CaregiverService service;
    @Test
    public void testAdd() throws Exception {
        Caregiver caregiver = new Caregiver();
        service.save(caregiver);//DomainService d = new
    }
}
