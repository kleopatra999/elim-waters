package com.church.elim.service;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.church.elim.controller.DataTablesRequest;
import com.church.elim.domain.Education;
import com.church.elim.domain.MaritalStatus;
import com.church.elim.domain.Caregiver;
import com.church.elim.domain.CaregiverQueryCondition;
import com.church.elim.domain.Person;
import com.church.elim.domain.views.CaregiverAdminView;
import com.church.elim.repository.CaregiverRepository;
import com.church.elim.repository.PersonRepository;
import com.church.elim.utils.ExcelImport;
@Service
public class CaregiverService extends DomainService<Caregiver>{
}
