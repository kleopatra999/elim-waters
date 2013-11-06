package com.church.elim.web.controller.rest;


import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.church.elim.domain.Caregiver;

@Secured("ROLE_USER")
@Controller
@RequestMapping("/caregivers")
public class RestCaregiverController extends RestDomainController<Caregiver>{
}
