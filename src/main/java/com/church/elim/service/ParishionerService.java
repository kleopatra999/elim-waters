package com.church.elim.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.church.elim.domain.Parishioner;
import com.church.elim.domain.Education;
import com.church.elim.domain.MaritalStatus;
import com.church.elim.domain.ParishionerQueryCondition;
import com.church.elim.domain.Person;
import com.church.elim.domain.views.ParishionerAdminView;
import com.church.elim.repository.ParishionerRepository;
import com.church.elim.repository.PersonRepository;
import com.church.elim.utils.ExcelImport;

@Service
public class ParishionerService {
	@Autowired 
	ParishionerRepository parishionerRepo;
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
			if (r == 0) {
				continue;
			}
			Parishioner parishioner = getParishioner(row);
			parishionerRepo.saveAndFlush(parishioner);
			System.out.println("Added " + parishioner.getFirstName() + 
					" " + parishioner.getLastName());
		}

	}

	@Transactional
	public Parishioner addParishioner(Parishioner parishioner) throws Exception {
		String firstName = parishioner.getFirstName(); 
		String lastName = parishioner.getLastName(); 
		if(firstName == null || firstName.equals("") || 
				firstName == null || firstName.equals("")){

			throw new Exception("Please provide first name and last name!");
		}
		List<Parishioner> parishioners = parishionerRepo.findByFirstNameAndLastName(firstName, lastName);
		if(parishioners != null && parishioners.size() > 0){
			throw new Exception("Person " + firstName + " " + lastName + " already exists!");
		}
		/**
		 * Sometimes the person can be added independent from the parishioner 
		 * (e.g. adding new children), therefore we must check if the person already 
		 * exists before creating a new one when we save a new parishioner.
		 */
		Person p = parishioner.getPerson();
		List<Person> result = personRepo.findByFirstNameAndLastName(parishioner.getFirstName(),
				parishioner.getLastName()); 
		if(result!=null && result.size()>0){
			p = result.get(0);
		}
		parishioner.setPerson(p);
		Person baptist = parishioner.getBaptist();
		// Sometimes the client sends a serialized baptist object with null values instead of null value
		if(baptist!=null && baptist.getLastName()==null){
			parishioner.setBaptist(null);
		}
		return parishionerRepo.saveAndFlush(parishioner);
	}

	/**
	 * Converts Excel row into Parishioner entity so we can persist it into MySQL db.
	 * @param row
	 * @return
	 * @throws ParseException
	 */
	private Parishioner getParishioner(XSSFRow row) throws ParseException{
		Parishioner p = new Parishioner();
		String firstName = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.PRENUME))); 
		p.setFirstName(firstName);
		String lastName = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.NUME))); 
		p.setLastName(lastName);

		String baptismDate = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.D_BOTEZ)));
		Date d = (baptismDate==null)?null:HSSFDateUtil.getJavaDate(Double.valueOf(baptismDate));
		System.out.println("bd: " + d);
		//DateFormatter df = new DateFormatter("mm/dd/yyyy");
		p.setBaptismDate(d);

		String maritalStatus = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.STARE_CIV)));
		p.setMaritalStatus(getMaritalStatus(maritalStatus));

		String phone = ExcelImport.getCellValue(row.getCell(
				cellNo(OldColumn.TELEFON)));
		p.setPhone(phone);

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
		p.setAddress(address);
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

	public String getParishionersFromExcel(InputStream is) throws IOException{
		return ExcelImport.readParishionerFromExcelFile(is);
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

	/* Queries parishioners which contain the serachText in one of the filter columns 
	 * and returns the requested page form the result.
	 */
	public List<ParishionerAdminView> getParishionersAdminView(DataTablesRequest dtReq) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ParishionerAdminView> criteria = builder.createQuery(ParishionerAdminView.class);
		Root<Parishioner> root = criteria.from(Parishioner.class);

		criteria = criteria.select(builder.construct(ParishionerAdminView.class,ParishionerAdminView.selectAllPath(root)));

		if(dtReq.sortedColumns != null 
				&& dtReq.sortedColumns.size() > 0){
			Integer fieldIndex= dtReq.sortedColumns.get(0) - 1;
			// DataTables sends sortedColumns=[0] by default, but we don't want any sorting by default
			if(fieldIndex > 0){
				Boolean asc = (dtReq.sortDirections.get(0).equals("asc"))?true:false;
				if(asc){
					criteria.orderBy(builder.asc(ParishionerAdminView.getSelectPath(root, fieldIndex)));
				} else{
					criteria.orderBy(builder.desc(ParishionerAdminView.getSelectPath(root, fieldIndex)));
				}
			}
		}
		Map<ParameterExpression<String>, String> paramsList = new HashMap<ParameterExpression<String>, String>();

		addGenericSearch(builder, criteria, root, paramsList, dtReq);

		TypedQuery<ParishionerAdminView> query =  entityManager.createQuery(criteria);
		for (ParameterExpression<String> param: paramsList.keySet())
			query.setParameter(param, paramsList.get(param).trim());

		if (dtReq.searchQuery == "")
			return query.getResultList();

		List<ParishionerAdminView> parishioners = new ArrayList<ParishionerAdminView>(); 

		for (ParishionerAdminView parishioner:query.getResultList())
			if (parishioner.contains(dtReq.searchQuery))
				parishioners.add(parishioner);

		return parishioners;
	}

	/* Queries parishioners which contain the serachText in one of the filter columns 
	 * and returns the requested page form the result.
	 */
	public List<ParishionerAdminView> getAllParishionersAdminView() {
		System.out.println("Just testing");
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ParishionerAdminView> criteria = builder.createQuery(ParishionerAdminView.class);
		Root<Parishioner> root = criteria.from(Parishioner.class);
		criteria = criteria.select(builder.construct(ParishionerAdminView.class,ParishionerAdminView.selectAllPath(root)));
		TypedQuery<ParishionerAdminView> query =  entityManager.createQuery(criteria);
		return query.getResultList();
	}
	/* Queries parishioners which contain the searchText in one of the filter columns. 
	 */
	public List<Parishioner> getFilteredData(DataTablesRequest dtReq) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Parishioner> criteria = builder.createQuery(Parishioner.class);
		Root<Parishioner> root = criteria.from(Parishioner.class);
		Map<ParameterExpression<String>, String> paramsList = new HashMap<ParameterExpression<String>, String>();
		addGenericSearch(builder, criteria, root, paramsList, dtReq);
		TypedQuery<Parishioner> query =  entityManager.createQuery(criteria);
		for (ParameterExpression<String> param: paramsList.keySet())
			query.setParameter(param, paramsList.get(param).trim());

		if (dtReq.searchQuery == "")
			return query.getResultList();

		List<Parishioner> parishioners = new ArrayList<Parishioner>(); 

		for (Parishioner parishioner:query.getResultList())
			if (parishioner.contains(dtReq.searchQuery))
				parishioners.add(parishioner);

		return parishioners;
	}

	/* Add the where clause to a CriteriaQuery. This way we can reuse this code for other Parishioner views,
	 * not only the ParishionerAdminView.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addGenericSearch(CriteriaBuilder builder,
			CriteriaQuery criteria,
			Root<Parishioner> root,
			Map<ParameterExpression<String>, String> paramsList,
			DataTablesRequest dtReq){
		List<Predicate> params = new ArrayList<Predicate>();
		String field, format;
		for (ParishionerQueryCondition condition: dtReq.searchCriteria){
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
		}

		if (params.size() == 1) {
			criteria = criteria.where(params.get(0));
		} else {
			criteria = criteria.where(builder.and(params.toArray(new Predicate[0])));
		}
	}

	@SuppressWarnings("rawtypes")
	private Path<String> getSelectPath(Root<Parishioner> root, String field){
		Path<String> path = null; 
		Metamodel model = entityManager.getMetamodel();
		EntityType<Parishioner> Parishioner_ = model.entity(Parishioner.class);
		EntityType<Person> Person_ = model.entity(Person.class);
		for(Attribute a: Parishioner_.getSingularAttributes()){
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
			throw new IllegalArgumentException(field + " is not an attribute of the Parishioner entity.");
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
	public Parishioner updateParishioner(Parishioner parishioner) {
		Person baptist = parishioner.getBaptist();
		// Sometimes the client sends a serialized baptist object with null values instead of null value
		if(baptist!=null && baptist.getLastName()==null){
			parishioner.setBaptist(null);
		}
		Parishioner updatedParishioner = parishionerRepo.saveAndFlush(parishioner);
		return updatedParishioner;
	}

	@Transactional
	public Parishioner updateBaptist(Long parishionerId, Long baptistId) {
		Parishioner parishioner = parishionerRepo.findOne(parishionerId);
		Parishioner updatedParishioner = parishioner;
		if(baptistId >= 0){
			Person baptist = personRepo.findOne(baptistId);
			parishioner.setBaptist(baptist);
			updatedParishioner = parishionerRepo.saveAndFlush(parishioner);
		}
		return updatedParishioner;
	}

	@Transactional
	public void deleteParishionersByIdGreatherThan(Long id){
		parishionerRepo.deleteStartingFrom(id);
		personRepo.deleteByIdGreatherThan(id);
	}

	public void removeParishioner(Parishioner parishioner) {
		parishionerRepo.delete(parishioner);
	}

	@Transactional
	public Person updateBaptist(Long parishionerId, String baptistName) throws Exception {
		Person baptist = null;
		if(baptistName!=null && !baptistName.trim().equals("")){
			Parishioner parishioner = parishionerRepo.findOne(parishionerId);
			String firstName = PersonService.getFirstName(baptistName);
			String lastName = PersonService.getLastName(baptistName);
			List<Person> results = personRepo.findByFirstNameAndLastName(firstName, lastName);
			// Create baptist person if not found
			if(results == null || results.size()==0){
				Person p = new Person(firstName, lastName);
				baptist = personRepo.saveAndFlush(p);
			}else{
				baptist = results.get(0);
			}
			parishioner.setBaptist(baptist);
			parishionerRepo.saveAndFlush(parishioner);
		}
		return baptist;
	}

	public List<Parishioner> findPaginated(Integer page, Integer size) {
		Query typedQuery = entityManager.createQuery("SELECT o FROM Parishioner o", Parishioner.class);

		typedQuery.setFirstResult((page-1)*size);
		List<Parishioner> resultList = typedQuery.getResultList();
		return resultList;
	}
}
