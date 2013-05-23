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
public class CaregiverService {
	@Autowired 
	CaregiverRepository caregiverRepo;
	@Autowired 
	PersonRepository personRepo;
	@PersistenceContext
	public EntityManager entityManager;

	enum OldColumn{NUME,PRENUME,D_NASTERE,LOC_NAST,NRCRT,PREN_TATA,PREN_MAMA,BIS_PARint,D_BOTEZ,
		BIS_BOTEZ,PAST_BOTEZ,D_PRIM_BIS,ACT_PRIMIR,LOCALITATE,STRADA,NUMAR,BLOC,ETAJ,APARTAMENT,
		TELEFON,STARE_CIV,PREN_SOT,TOTALDON,TOTALCON}

	/**
	 * Imports data from Excel files containing data in old format.
	 * @param filePath
	 * @throws IOException
	 * @throws ParseException
	 */
	public void importDataFromExcelFile(String filePath) throws IOException, ParseException{
		System.out.println("Excel file:" + filePath);
		FileInputStream fileInputStream = new FileInputStream(filePath);

		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet = workbook.getSheetAt(0);

		int rows = sheet.getPhysicalNumberOfRows() + 5;
		System.out.println(workbook.getSheetName(0) + "\" has " + rows + " row(s).");
		for (int r = 0; r < rows; r++) {
			XSSFRow row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			// Skip Header Row
			if (r == 0) {
				continue;
			}
			Caregiver caregiver = getCaregiver(row);
			caregiverRepo.saveAndFlush(caregiver);
			System.out.println("Added " + caregiver.getPerson().getFirstName() + 
					" " + caregiver.getPerson().getLastName());
		}

	}

	/**
	 * Converts Excel row into Caregiver entity so we can persist it into MySQL db.
	 * @param row
	 * @return
	 * @throws ParseException
	 */
	private Caregiver getCaregiver(XSSFRow row) throws ParseException{
		Caregiver p = new Caregiver();
		p.setPerson(new Person()); 
		String firstName = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.PRENUME))); 
		p.getPerson().setFirstName(firstName);
		String lastName = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.NUME))); 
		p.getPerson().setLastName(lastName);

		String baptismDate = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.D_BOTEZ)));
		Date d = (baptismDate==null)?null:HSSFDateUtil.getJavaDate(Double.valueOf(baptismDate));
		System.out.println("bd: " + d);
		//DateFormatter df = new DateFormatter("mm/dd/yyyy");
		
		String maritalStatus = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.STARE_CIV)));
		p.getPerson().setMaritalStatus(getMaritalStatus(maritalStatus));

		String phone = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.TELEFON)));
		p.getPerson().setPhone(phone);

		String strada = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.STRADA)));
		String nr = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.NUMAR)));
		String bloc = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.BLOC)));
		String etaj = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.ETAJ)));
		String apt = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.APARTAMENT)));

		String localitate = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.LOCALITATE)));

		String address = "Str. " + strada + 
				", Nr. " + nr + ", Bloc " + bloc + ", Etaj " + etaj + ", Apt. " + apt
				+ ", " + localitate;
		address = address.replace("null", "-");
		p.getPerson().setAddress(address);
		return p;
	}

	private Integer cellNo(OldColumn column){
		Integer colNo = null;
		OldColumn[] cols = OldColumn.values(); 
		for(int i=0;i<cols.length;i++){
			if(cols[i]==column){
				colNo = i;
			}
		}
		return colNo;
	}

	public String getCaregiversFromExcel(InputStream is) throws IOException{
		return null;//ExcelImport.readCaregiverFromExcelFile(is);
	}

	private String getMaritalStatus(String s){
		if ("1".equals(s)) {
			return "single";
		} else if ("c".equals(s)) {
			return "married";
		} else if ("r".equals(s)) {
			return "remarried";
		} else if ("v".equals(s)) {
			return "widow(er)";
		} else if ("d".equals(s)) {
			return "divorced";
		} else {
			return "";
		}
	}

	/* Queries caregivers which contain the serachText in one of the filter columns 
	 * and returns the requested page form the result.
	 */
	public List<CaregiverAdminView> getCaregiversAdminView(DataTablesRequest dtReq) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CaregiverAdminView> criteria = builder.createQuery(CaregiverAdminView.class);
		Root<Caregiver> root = criteria.from(Caregiver.class);
		
		criteria = criteria.select(builder.construct(CaregiverAdminView.class,CaregiverAdminView.selectAllPath(root)));
		
		if(dtReq.sortedColumns != null 
				&& dtReq.sortedColumns.size() > 0){
			Integer fieldIndex= dtReq.sortedColumns.get(0) - 1;
			// DataTables sends sortedColumns=[0] by default, but we don't want any sorting by default
			if(fieldIndex > 0){
				Boolean asc = (dtReq.sortDirections.get(0).equals("asc"))?true:false;
				if(asc){
					criteria.orderBy(builder.asc(CaregiverAdminView.getSelectPath(root, fieldIndex)));
				} else{
					criteria.orderBy(builder.desc(CaregiverAdminView.getSelectPath(root, fieldIndex)));
				}
			}
		}
		Map<ParameterExpression<String>, String> paramsList = new HashMap<ParameterExpression<String>, String>();
		
		addGenericSearch(builder, criteria, root, paramsList, dtReq);
		
		TypedQuery<CaregiverAdminView> query =  entityManager.createQuery(criteria);
		for (ParameterExpression<String> param: paramsList.keySet())
			query.setParameter(param, paramsList.get(param).trim());
		
		 if (dtReq.searchQuery == "")
			return query.getResultList();
		
		List<CaregiverAdminView> caregivers = new ArrayList<CaregiverAdminView>(); 
			
		for (CaregiverAdminView caregiver:query.getResultList())
			if (caregiver.contains(dtReq.searchQuery))
				caregivers.add(caregiver);
	
		return caregivers;
	}

	/* Queries caregivers which contain the serachText in one of the filter columns 
	 * and returns the requested page form the result.
	 */
	public List<CaregiverAdminView> getAllCaregiversAdminView() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CaregiverAdminView> criteria = builder.createQuery(CaregiverAdminView.class);
		Root<Caregiver> root = criteria.from(Caregiver.class);
		criteria = criteria.select(builder.construct(CaregiverAdminView.class,CaregiverAdminView.selectAllPath(root)));
		TypedQuery<CaregiverAdminView> query =  entityManager.createQuery(criteria);
		return query.getResultList();
	}
	/* Queries caregivers which contain the searchText in one of the filter columns. 
	 */
	public List<Caregiver> getFilteredData(DataTablesRequest dtReq) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Caregiver> criteria = builder.createQuery(Caregiver.class);
		Root<Caregiver> root = criteria.from(Caregiver.class);
		Map<ParameterExpression<String>, String> paramsList = new HashMap<ParameterExpression<String>, String>();
		addGenericSearch(builder, criteria, root, paramsList, dtReq);
		TypedQuery<Caregiver> query =  entityManager.createQuery(criteria);
		for (ParameterExpression<String> param: paramsList.keySet())
			query.setParameter(param, paramsList.get(param).trim());
		
		 if (dtReq.searchQuery == "")
			return query.getResultList();
		
		List<Caregiver> caregivers = new ArrayList<Caregiver>(); 
			
		for (Caregiver caregiver:query.getResultList())
			if (caregiver.contains(dtReq.searchQuery))
				caregivers.add(caregiver);
	
		return caregivers;
	}

	/* Add the where clause to a CriteriaQuery. This way we can reuse this code for other Caregiver views,
	 * not only the CaregiverAdminView.
	 */
	private void addGenericSearch(CriteriaBuilder builder,
			CriteriaQuery criteria,
			Root<Caregiver> root,
			Map<ParameterExpression<String>, String> paramsList,
			DataTablesRequest dtReq){
		List<Predicate> params = new ArrayList<Predicate>();
		String field, format;
		/*for (CaregiverQueryCondition condition: dtReq.searchCriteria){
			field = condition.getField().toString();
			format = condition.getValue().trim();
			if (field != null) {
			    ParameterExpression<String> param = builder.parameter(String.class, field);
			    if (format.startsWith("=")){
			    	params.add(builder.equal(getSelectPath(root, field), param));
			    	format = format.substring(1);
			    } else if (format.startsWith("!")) {
			    	params.add(builder.notLike(getSelectPath(root, field), param));
			    	format = format.substring(1);
			    } else
			    	params.add(builder.like(getSelectPath(root, field), param));
			    paramsList.put(param, format);
		    }
		}*/
		
		if (params.size() == 1) {
	        criteria = criteria.where(params.get(0));
	    } else {
	        criteria = criteria.where(builder.and(params.toArray(new Predicate[0])));
	    }
	}
	
	private Path<String> getSelectPath(Root<Caregiver> root, String field){
		Path<String> path = null; 
		Metamodel model = entityManager.getMetamodel();
		EntityType<Caregiver> Caregiver_ = model.entity(Caregiver.class);
		EntityType<Person> Person_ = model.entity(Person.class);
		for(Attribute a: Caregiver_.getSingularAttributes()){
			if(a.getName().equalsIgnoreCase(field)){
				path = root.<String>get(field);
			}
		}
		if(path==null){
			for(Attribute a: Person_.getSingularAttributes()){
				if(a.getName().equalsIgnoreCase(field)){
					path = root.<String>get("person").<String>get(field);
				}
			}	
		}
		if(path==null){
			throw new IllegalArgumentException(field + " is not an attribute of the Caregiver entity.");
		}
		return path;
	}
	
	public List<MaritalStatus> getMaritalStatusList(){
		return Arrays.asList(MaritalStatus.values());
	}
	
	public List<Person> getBaptistsList(){
		return personRepo.findAll();
	}
	
	public List<Education> getEducationList() {
		return Arrays.asList(Education.values());
	}

	@Transactional
	public Caregiver addCaregiver(Caregiver caregiver) throws Exception {
		String firstName = caregiver.getPerson().getFirstName(); 
		String lastName = caregiver.getPerson().getLastName(); 
		if(firstName == null || firstName.equals("") || 
				firstName == null || firstName.equals("")){
			
			throw new Exception("Please provide first name and last name!");
		}
		List<Caregiver> caregivers = caregiverRepo.findByFirstNameAndLastName(firstName, lastName);
		if(caregivers != null && caregivers.size() > 0){
			throw new Exception("Person " + firstName + " " + lastName + " already exists!");
		}
		Person p =caregiver.getPerson();
		caregiver.setPerson(personRepo.saveAndFlush(p));
		
		return caregiverRepo.saveAndFlush(caregiver);
	}
	
	@Transactional
	public Caregiver updateCaregiver(Caregiver caregiver) {
		/*String cnp = caregiver.getPerson().getCnp(); 
		if(cnp==null||cnp.trim().equals("")){
			throw new IllegalArgumentException("The CNP value is not set. Please set the CNP and retry!");
		}
		if (!isUniqueCnp(caregiver.getPerson().getCnp(), 
							caregiver.getId().toString()))
			throw new IllegalArgumentException("The CNP value is not unique.");*/
		Caregiver mergedCaregiver = entityManager.merge(caregiver);
		return mergedCaregiver;
	}
	
	private boolean isUnique(String cnp, String id){
		String queryString = "select count(*) from person where CNP is not null and CNP != \"\" and CNP=" + cnp;
		if (id.compareTo("") != 0)
			queryString += " and id_person != " + id;
		
		Query query = entityManager.createNativeQuery(queryString);
		if ( !((BigInteger) query.getSingleResult()).equals(BigInteger.valueOf(0)) )
			return false;
		
		return true;
	}
	
	@Transactional
	public void deleteCaregiversByIdGreatherThan(Long id){
		caregiverRepo.deleteByIdGreatherThan(id);
		personRepo.deleteByIdGreatherThan(id);
	}
}
