package com.synectiks.procurement.business.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Currency;
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionActivity;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.RequisitionLineItemActivity;
import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.domain.Vendor;
import com.synectiks.procurement.domain.VendorRequisitionBucket;
import com.synectiks.procurement.repository.RequisitionActivityRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.repository.RequisitionRepository;
import com.synectiks.procurement.repository.VendorRequisitionBucketRepository;
import com.synectiks.procurement.util.DateFormatUtil;

@Service
public class RequisitionService {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionService.class);

	@Autowired
	private RequisitionRepository requisitionRepository;

	@Autowired
	private RequisitionActivityRepository requisitionActivityRepository;

	@Autowired
	private RequisitionActivityService requisitionActivityService;

//	@Autowired
//	private RequisitionLineItemService requisitionLineItemService;
	
	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private RequisitionLineItemService requisitionLineItemService;

	@Autowired
	private VendorRequisitionBucketRepository vendorRequisitionBucketRepository;

//	@Autowired
//	private DataFileRepository dataFileRepository;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private RolesService rolesService;

	@Autowired
	private RulesService rulesService;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private RequisitionLineItemActivityService requisitionLineItemActivityService;
	
	@Autowired
	private RequisitionLineItemRepository requisitionLineItemRepository;
	
	@Autowired
    private XformAwsS3Config xformAwsS3Config;
	
	public Requisition getRequisition(Long id) {
		logger.info("Getting requisition by id: " + id);
		Optional<Requisition> ovn = requisitionRepository.findById(id);
		if (ovn.isPresent()) {
			logger.info("Requisition: " + ovn.get().toString());
			Requisition req = ovn.get();
			return req;
		}
		logger.warn("Requisition not found");
		return null;
	}

	@Transactional
	public Requisition addRequisition(MultipartFile[] extraBudgetoryFile, MultipartFile[] requisitionLineItemFile,
			String obj) throws JsonMappingException, JsonProcessingException, JSONException, IOException, ParseException {
		logger.info("Adding requistion");
		Requisition requisition = new Requisition();

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = (ObjectNode) mapper.readTree(obj);

		if (json.get("departmentId") != null) {
			Department department = departmentService.getDepartment(Long.parseLong(json.get("departmentId").asText()));
			if (department != null) {
				requisition.setDepartment(department);
			}
		}

		if (json.get("currencyId") != null) {
			Currency currency = currencyService.getCurrency(Long.parseLong(json.get("currencyId").asText()));
			if (currency != null) {
				requisition.setCurrency(currency);
			}
		}

//		if (json.get("requisitionNo") != null) {
//			requisition.setRequisitionNo(json.get("requisitionNo").asText());
//		}

		requisition.setProgressStage(Constants.PROGRESS_STAGE_NEW);

		if (json.get("financialYear") != null) {
			requisition.setFinancialYear(json.get("financialYear").asInt());
		}

		Rules rule = rulesService.getRulesByName(Constants.RULE_REQUISITION_TYPE);
////		if (json.get("roleName").asText() != null) {
////			Roles role = rolesService.getRolesByName(json.get("roleName").asText());
////			rule = rulesService.getRulesByRoleAndRuleName(role, Constants.RULE_REQUISITION_TYPE);
////		} else {
////			logger.error("Requistion could not be added. User's role missing");
////			return null;
////		}
		if (rule != null) {
			JSONObject ruleJson = new JSONObject(rule.getRule());
			JSONObject nonStandardRule = ruleJson.getJSONObject(Constants.REQUISITION_TYPE_NON_STANDARD);
			if (json.get("totalPrice") != null) {
				int price = json.get("totalPrice").asInt();
				if (price >= nonStandardRule.getInt("min") && price <= nonStandardRule.getInt("max")) {
					requisition.setType(Constants.REQUISITION_TYPE_NON_STANDARD);
				} else {
					requisition.setType(Constants.REQUISITION_TYPE_STANDARD);
				}
			} else {
				requisition.setType(Constants.REQUISITION_TYPE_NON_STANDARD);
			}
		} else {
			requisition.setType(Constants.REQUISITION_TYPE_NON_STANDARD);
		}

//		}

//		if (json.get("totalPrice") != null) {
//			requisition.setTotalPrice(json.get("totalPrice").asInt());
//		}

		if (json.get("notes") != null) {
			requisition.setNotes(json.get("notes").asText());
		}

		if (json.get("status") != null) {
			requisition.setStatus(json.get("status").asText());
		}

		setFromatedDueDate(requisition, json);

		if (json.get("user") != null) {
			requisition.setCreatedBy(json.get("user").asText());
			requisition.setUpdatedBy(json.get("user").asText());
		} else {
			requisition.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			requisition.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		requisition.setCreatedOn(now);
		requisition.setUpdatedOn(now);

		List<RequisitionLineItem> liteItemList = getLineItemFromFile(requisitionLineItemFile);

		List<RequisitionLineItem> liteItemList2 = getLineItemFromJson(obj);
		liteItemList.addAll(liteItemList2);

		int totalAmt = 0;
		for (RequisitionLineItem rqLnItm : liteItemList) {
			int amt = rqLnItm.getOrderQuantity() * rqLnItm.getRatePerItem();
			totalAmt = totalAmt + amt;
		}
		requisition.setTotalPrice(totalAmt);

		requisition = requisitionRepository.save(requisition);
		logger.info("Requisition added successfully");

		saveFile(extraBudgetoryFile, requisition, now, Constants.IDENTIFIER_REQUISITION_EXTRA_BUDGETORY_FILE);
		saveRequisitionActivity(requisition);
		saveRequisitionLineItem(requisition, liteItemList);
		
		saveFile(requisitionLineItemFile, requisition, now, Constants.IDENTIFIER_REQUISITION_LINE_ITEM_FILE);
		logger.info("Requisition added successfully");
		
		return requisition;
	}

	private void setFromatedDueDate(Requisition requisition, ObjectNode json) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if (json.get("dueDate") != null) {
			try {
				LocalDate localDate = LocalDate.parse(json.get("dueDate").asText(), formatter);
				requisition.setDueDate(localDate);
			} catch (Exception e) {
				logger.error("Cannot read due date. Exception: " + e.getMessage());
				long millis = System.currentTimeMillis();
				java.sql.Date date = new java.sql.Date(millis);

				LocalDate datew = LocalDate.parse(date.toString());
				LocalDate localDate = datew.plusDays(Constants.DEFAULT_DUE_DAYS);
				requisition.setDueDate(localDate);
			}
		} else {
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);

			LocalDate datew = LocalDate.parse(date.toString());
			LocalDate localDate = datew.plusDays(Constants.DEFAULT_DUE_DAYS);
			requisition.setDueDate(localDate);
		}
	}

	@Transactional
	public Requisition updateRequisition(MultipartFile[] extraBudgetoryFile, MultipartFile[] requisitionLineItemFile,
			String obj) throws JsonMappingException, JsonProcessingException, JSONException, IOException {
		logger.info("Update requisition");

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = (ObjectNode) mapper.readTree(obj);

		Optional<Requisition> orq = requisitionRepository.findById(Long.parseLong(json.get("id").asText()));
		if (!orq.isPresent()) {
			logger.error("Requisition could not be updated. Requisition not found");
			return null;
		}

		Requisition requisition = orq.get();

		if (json.get("financialYear") != null) {
			requisition.setFinancialYear(json.get("financialYear").asInt());
		}

		if (json.get("departmentId") != null) {
			Department department = departmentService.getDepartment(Long.parseLong(json.get("departmentId").asText()));
			if (department != null) {
				requisition.setDepartment(department);
			}
		}

		if (json.get("currencyId") != null) {
			Currency currency = currencyService.getCurrency(Long.parseLong(json.get("currencyId").asText()));
			if (currency != null) {
				requisition.setCurrency(currency);
			}
		}

		if (json.get("notes") != null) {
			requisition.setNotes(json.get("notes").asText());
		}

		if (json.get("progressStage") != null) {
			requisition.setProgressStage(json.get("progressStage").asText());
		}

//		Rules rule = null; 
//		if (obj.get("roleName").asText() != null) {
//			Roles role = rolesService.getRolesByName(obj.get("roleName").asText());
//			rule = rulesService.getRulesByRoleAndRuleName(role, Constants.RULE_REQUISITION_TYPE);
//		} else {
//			logger.error("Requistion could not be added. User's role missing");
//			return null;
//		}

//		JSONObject jsonObject = new JSONObject(rule.getRule());
//		JSONObject nonStandardRule = jsonObject.getJSONObject(Constants.REQUISITION_TYPE_NON_STANDARD);
//		if (obj.get("totalPrice") != null) {
//			int price = obj.get("totalPrice").asInt();
//			if (price >= nonStandardRule.getInt("min") && price <= nonStandardRule.getInt("max")) {
//				requisition.setType(Constants.REQUISITION_TYPE_NON_STANDARD);
//			} else {
//				requisition.setType(Constants.REQUISITION_TYPE_STANDARD);
//			}
//		}

		if (json.get("status") != null) {
			requisition.setStatus(json.get("status").asText());
		}

//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
//		if (obj.get("dueDate") != null) {
//			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
//			requisition.setDueDate(localDate);
//		}

		if (json.get("user") != null) {
			requisition.setUpdatedBy(json.get("user").asText());
		} else {
			requisition.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		requisition.setUpdatedOn(now);

		if (requisitionLineItemFile != null) {

			List<RequisitionLineItem> liteItemList = getLineItemFromFileForUpdate(requisitionLineItemFile);
			List<RequisitionLineItem> liteItemList2 = getLineItemFromJson(obj);
			liteItemList.addAll(liteItemList2);
			int totalAmt = 0;
			for (RequisitionLineItem rqLnItm : liteItemList) {
				int amt = rqLnItm.getOrderQuantity() * rqLnItm.getRatePerItem();
				totalAmt = totalAmt + amt;
			}
			requisition.setTotalPrice(totalAmt);
			saveRequisitionLineItem(requisition, liteItemList);
//			saveRequisitionLineItemFile(requisitionLineItemFile, now);
			saveFile(requisitionLineItemFile, requisition, now, Constants.IDENTIFIER_REQUISITION_LINE_ITEM_FILE);
		}

		if (extraBudgetoryFile != null) {
			saveFile(extraBudgetoryFile, requisition, now, Constants.IDENTIFIER_REQUISITION_EXTRA_BUDGETORY_FILE);
		}

		if (json.get("status").asText().equals(Constants.STATUS_DEACTIVE)) {

			HashMap<String, String> mp = new HashMap<String, String>();
			mp.put("requisitionId", json.get("id").asText());
			List<RequisitionLineItem> list = requisitionLineItemService
					.searchRequisitionLineItem(mp);

			for (RequisitionLineItem requisitionLineItem : list) {
				requisitionLineItem.setStatus(Constants.STATUS_DEACTIVE);
				
				requisitionLineItem = requisitionLineItemRepository.save(requisitionLineItem);
				
				requisitionLineItemService.addRequisitionLineItem(requisitionLineItem);
				
			}
		}
		requisition = requisitionRepository.save(requisition);
		logger.info("Requisition updated successfully");
		saveRequisitionActivity(requisition);

		return requisition;
	}

	private void saveRequisitionLineItem(Requisition requisition, List<RequisitionLineItem> liteItemList) {
		logger.info("Saving requisition line items");
		for (RequisitionLineItem reqLineItem : liteItemList) {
			logger.debug("Requisition line item: " + reqLineItem.toString());
			reqLineItem.setRequisition(requisition);
			if (reqLineItem.getId() == null) {
				reqLineItem.setCreatedBy(requisition.getCreatedBy());
				reqLineItem.setCreatedOn(requisition.getCreatedOn());
			}
			reqLineItem.setUpdatedBy(requisition.getUpdatedBy());
			reqLineItem.setUpdatedOn(requisition.getUpdatedOn());
			reqLineItem.setStatus(requisition.getStatus());
			reqLineItem = requisitionLineItemService.addRequisitionLineItem(reqLineItem);
			requisition.getRequisitionLineItemLists().add(reqLineItem);
		}
		logger.info("Requisition line items saved successfully");
	}

	private List<RequisitionLineItem> getLineItemFromJson(String obj) throws JSONException {
		List<RequisitionLineItem> liteItemList = new ArrayList<>();
		JSONObject jsonObj = new JSONObject(obj);
		JSONArray reqLineItemArray = jsonObj.getJSONArray("requisitionLineItemLists");
		if (reqLineItemArray != null && reqLineItemArray.length() > 0) {
			for (int j = 0; j < reqLineItemArray.length(); j++) {
				JSONObject json = reqLineItemArray.getJSONObject(j);
				RequisitionLineItem reqLineItem = new RequisitionLineItem();

				if (!json.isNull("id")) {
					try {
						String q = (String) json.get("id");
						reqLineItem = this.requisitionLineItemService.getRequisitionLineItem(Long.parseLong(q));
					} catch (Exception e) {
						try {
							Integer q = (Integer) json.get("id");
							reqLineItem = this.requisitionLineItemService.getRequisitionLineItem(q.longValue());
						} catch (Exception ee) {
							logger.error("Cannot read line item id. Exception ", e);
							throw ee;
						}
					}
				}

				if (!json.isNull("orderQuantity")) {
					try {
						String q = (String) json.get("orderQuantity");
						reqLineItem.setOrderQuantity(Integer.parseInt(q));
					} catch (Exception e) {
						try {
							Integer q = (Integer) json.get("orderQuantity");
							reqLineItem.setOrderQuantity(q);
						} catch (Exception ee) {
							logger.error("Cannot read quantity. Exception ", e);
							throw ee;
						}
					}
				}

				if (!json.isNull("itemDescription")) {
					reqLineItem.setItemDescription((String) json.get("itemDescription"));
				}
				if (!json.isNull("ratePerItem")) {
					try {
						String q = (String) json.get("ratePerItem");
						reqLineItem.setRatePerItem(Integer.parseInt(q));
					} catch (Exception e) {
						try {
							Integer q = (Integer) json.get("ratePerItem");
							reqLineItem.setRatePerItem(q);
						} catch (Exception ee) {
							logger.error("Cannot read per item rate. Exception ", e);
							throw ee;
						}
					}
				}

				if (reqLineItem.getOrderQuantity() != null && reqLineItem.getRatePerItem() != null) {
					int amt = reqLineItem.getOrderQuantity() * reqLineItem.getRatePerItem();
					reqLineItem.setPrice(amt);
				}

				liteItemList.add(reqLineItem);
			}
		}
		return liteItemList;
	}

	public List<RequisitionLineItem> getLineItemFromFile(MultipartFile[] requisitionLineItemFile) throws IOException {
		List<RequisitionLineItem> lineItemList = new ArrayList<>();
		if (requisitionLineItemFile == null) {
			return lineItemList;
		}
		for (MultipartFile file : requisitionLineItemFile) {
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet worksheet = workbook.getSheetAt(0);

			// skipping first row, that is row index 0. Starting loop with 1
			for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = worksheet.getRow(i);
				RequisitionLineItem item = new RequisitionLineItem();

				if (row.getCell(0) != null) {
					item.setItemDescription(row.getCell(0).getStringCellValue());
				}

				try {
					if (row.getCell(1) != null) {
						item.setOrderQuantity((int) row.getCell(1).getNumericCellValue());
					}
				} catch (Exception e) {
					if (row.getCell(1) != null) {
						item.setOrderQuantity(Integer.parseInt(row.getCell(1).getStringCellValue()));
					}
				}

				try {
					if (row.getCell(2) != null) {
						item.setRatePerItem((int) row.getCell(2).getNumericCellValue());
					}
				} catch (Exception e) {
					if (row.getCell(2) != null) {
						item.setRatePerItem(Integer.parseInt(row.getCell(2).getStringCellValue()));
					}
				}

				if (item.getOrderQuantity() != null && item.getRatePerItem() != null) {
					int amt = item.getOrderQuantity() * item.getRatePerItem();
					item.setPrice(amt);
				}

//			try {
//				if (row.getCell(3) != null) {
//					item.setPrice((int) row.getCell(3).getNumericCellValue());
//				} 
//			}catch(Exception e) {
//				if (row.getCell(3) != null) {
//					item.setPrice(Integer.parseInt(row.getCell(3).getStringCellValue()));
//				}
//			}

				lineItemList.add(item);

			}
		}
		return lineItemList;

	}
	
	public List<RequisitionLineItem> getLineItemFromFileForUpdate(MultipartFile[] requisitionLineItemFile) throws IOException {
		List<RequisitionLineItem> lineItemList = new ArrayList<>();
		if (requisitionLineItemFile == null) {
			return lineItemList;
		}
		for (MultipartFile file : requisitionLineItemFile) {
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet worksheet = workbook.getSheetAt(0);

			// skipping first row, that is row index 0. Starting loop with 1
			for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = worksheet.getRow(i);
				RequisitionLineItem item = new RequisitionLineItem();

				if (row.getCell(0).getCellType() != null) {
					item.setItemDescription(row.getCell(0).getStringCellValue());
				}

				try {
					if (row.getCell(1) != null) {
						item.setOrderQuantity((int) row.getCell(1).getNumericCellValue());
					}
				} catch (Exception e) {
					if (row.getCell(1) != null) {
						item.setOrderQuantity(Integer.parseInt(row.getCell(1).getStringCellValue()));
					}
				}

				try {
					if (row.getCell(2) != null) {
						item.setRatePerItem((int) row.getCell(2).getNumericCellValue());
					}
				} catch (Exception e) {
					if (row.getCell(2) != null) {
						item.setRatePerItem(Integer.parseInt(row.getCell(2).getStringCellValue()));
					}
				}
				
				try {
					if (row.getCell(3) != null) {
						item.setId( (long) row.getCell(3).getNumericCellValue());
					}
				} catch (Exception e) {
					if (row.getCell(3) != null) {
						item.setId(Long.parseLong( row.getCell(3).getStringCellValue()));
					}
				}


				if (item.getOrderQuantity() != null && item.getRatePerItem() != null) {
					int amt = item.getOrderQuantity() * item.getRatePerItem();
					item.setPrice(amt);
				}


				lineItemList.add(item);

			}
		}
		return lineItemList;

	}

	private RequisitionActivity saveRequisitionActivity(Requisition requisition) {
		RequisitionActivity requisitionActivity = null;
		if (requisition != null) {
			logger.info("Adding requisition activity");
			requisitionActivity = new RequisitionActivity();
			BeanUtils.copyProperties(requisition, requisitionActivity);
			requisitionActivity.setRequisitionId(requisition.getId());

			requisitionActivity.setUpdatedBy(requisition.getUpdatedBy());
			requisitionActivity.setUpdatedOn(requisition.getUpdatedOn());

			requisitionActivity = requisitionActivityService.addRequisitionActivity(requisitionActivity);
			logger.info("Requisition activity added successfully");
		}
		return requisitionActivity;
	}

	public List<Requisition> searchRequisition(Map<String, String> requestObj) throws ParseException {
		logger.info("Request to search requisition on given filter criteria");
		Requisition requisition = new Requisition();

		boolean isFilter = false;
		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("id"))) {
			requisition.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("reqNo"))) {
			requisition.setId(Long.parseLong(requestObj.get("reqNo").toLowerCase()));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("reqestNo"))) {
			requisition.setId(Long.parseLong(requestObj.get("reqestNo").toLowerCase()));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("departmentId"))) {
			Department department = departmentService.getDepartment(Long.parseLong(requestObj.get("departmentId")));
			if (department != null) {
				requisition.setDepartment(department);
			}
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("currencyId"))) {
			Currency currency = currencyService.getCurrency(Long.parseLong(requestObj.get("currencyId")));
			if (currency != null) {
				requisition.setCurrency(currency);
			}
			isFilter = true;
		}

//		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("requisitionNo"))) {
//			requisition.setRequisitionNo(requestObj.get("requisitionNo"));
//			isFilter = true;
//		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("status"))) {
			requisition.setStatus(requestObj.get("status"));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("progressStage"))) {
			requisition.setProgressStage(requestObj.get("progressStage"));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("financialYear"))) {
			requisition.setFinancialYear(Integer.parseInt(requestObj.get("financialYear")));
			isFilter = true;
		}
		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("type"))) {
			requisition.setType(requestObj.get("type"));
			isFilter = true;
		}
		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("totalPrice"))) {
			requisition.setTotalPrice(Integer.parseInt(requestObj.get("totalPrice")));
			isFilter = true;
		}
		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("notes"))) {
			requisition.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant instant = Instant.parse(requestObj.get("createdOn"));
			requisition.setCreatedOn(instant);
			isFilter = true;
		}
		if (requestObj.get("createdBy") != null) {
			requisition.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant instant = Instant.parse(requestObj.get("updatedOn"));
			requisition.setUpdatedOn(instant);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			requisition.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("dueDate"))) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
			LocalDate localDate = LocalDate.parse(requestObj.get("dueDate"), formatter);
			requisition.setDueDate(localDate);
			isFilter = true;
		}

		List<Requisition> list = null;
		if (isFilter) {
			list = this.requisitionRepository.findAll(Example.of(requisition), Sort.by(Direction.DESC, "id"));
		} else {list = this.requisitionRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		Date fromDate = null;
		boolean isDateFilter = false;
		if (requestObj.get("fromDate") != null) {
			fromDate = DateFormatUtil.convertStringToUtilDate(Constants.DEFAULT_DATE_FORMAT,
					requestObj.get("fromDate"));
			isDateFilter = true;
		}

		Date toDate = null;
		if (requestObj.get("toDate") != null) {
			toDate = DateFormatUtil.convertStringToUtilDate(Constants.DEFAULT_DATE_FORMAT, requestObj.get("toDate"));
			isDateFilter = true;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.from(ZoneOffset.UTC));
		List<Requisition> filteredList = new ArrayList<>();
		for (Requisition req : list) {
			if (fromDate != null && toDate != null && fromDate.equals(toDate)) {
				Date reqDate = DateFormatUtil.convertInstantToUtilDate(formatter, req.getCreatedOn());
				if (reqDate.equals(fromDate)) {
					filteredList.add(req);
				}
			} else if (fromDate != null && toDate != null && !fromDate.equals(toDate)) {
				Date reqDate = DateFormatUtil.convertInstantToUtilDate(formatter, req.getCreatedOn());
				if (reqDate.getTime() >= fromDate.getTime() && reqDate.getTime() <= toDate.getTime()) {
					filteredList.add(req);
				}
			} else if (fromDate != null && toDate == null) {
				Date reqDate = DateFormatUtil.convertInstantToUtilDate(formatter, req.getCreatedOn());
				if (reqDate.getTime() >= fromDate.getTime()) {
					filteredList.add(req);
				}
			} else if (fromDate == null && toDate != null) {
				Date reqDate = DateFormatUtil.convertInstantToUtilDate(formatter, req.getCreatedOn());
				if (reqDate.getTime() <= toDate.getTime()) {
					filteredList.add(req);
				}
			}
		}

		if (isDateFilter) {
			list = filteredList;
		}

		for (Requisition req : list) {
			RequisitionActivity ca = new RequisitionActivity();
			ca.setRequisitionId(req.getId());
			List<RequisitionActivity> caList = requisitionActivityRepository.findAll(Example.of(ca));
			req.setActivityList(caList);

			Map<String, String> searchDoc = new HashMap<>();
			searchDoc.put("sourceOfOrigin", this.getClass().getSimpleName());
			searchDoc.put("sourceId", String.valueOf(req.getId()));
//			searchDoc.put("identifier", Constants.IDENTIFIER_REQUISITION_EXTRA_BUDGETORY_FILE);
			List<Document> docList = documentService.searchDocument(searchDoc);
			req.setDocumentList(docList);

			Map<String, String> searchLineItem = new HashMap<>();
			searchLineItem.put("requisitionId", String.valueOf(req.getId()));
			List<RequisitionLineItem> lineItemList = requisitionLineItemService
					.searchRequisitionLineItem(searchLineItem);
			req.setLineItemList(lineItemList);
		}

		logger.info("Requisition search completed. Total records: " + list.size());

		return list;

	}

//	public void deleteRequisition(Long id) {
//		requisitionRepository.deleteById(id);
//		logger.info("Requisition deleted successfully");
//	}

	public List<Requisition> getAllRequisitions() throws ParseException {
		Map<String, String> requestObj = new HashMap<>();
		List<Requisition> list = searchRequisition(requestObj);
//		List<Requisition> list = requisitionRepository.findAll(Sort.by(Direction.ASC, "id"));
		logger.info("All requisitions. Total records: " + list.size());
		return list;
	}

	@Transactional
	public void sendRequisitionToVendor(List<ObjectNode> list) {
		for (ObjectNode obj : list) {
			VendorRequisitionBucket bucket = new VendorRequisitionBucket();
			logger.debug("Send new requisition to vendor: " + obj.toString());
			if (obj.get("requisitionId") != null) {
				Optional<Requisition> or = requisitionRepository.findById(obj.get("requisitionId").asLong());
				if (or.isPresent()) {
					bucket.setRequisition(or.get());
				}
			}
			if (obj.get("vendorId") != null) {
				Vendor vendor = vendorService.getVendor(obj.get("vendorId").asLong());

				if (vendor != null) {
					bucket.setVendor(vendor);
				}
			}
			if (obj.get("stages") != null) {
				bucket.setStages(obj.get("stages").asText());
			}
			if (obj.get("notes") != null) {
				bucket.setNotes(obj.get("notes").asText());
			}
			if (obj.get("status") != null) {
				bucket.setStatus(obj.get("status").asText());
			}
			if (obj.get("user") != null) {
				bucket.setCreatedBy(obj.get("user").asText());
				bucket.setUpdatedBy(obj.get("user").asText());
			} else {
				bucket.setCreatedBy(Constants.SYSTEM_ACCOUNT);
				bucket.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			}

			Instant now = Instant.now();
			bucket.setCreatedOn(now);
			bucket.setUpdatedOn(now);
			bucket = vendorRequisitionBucketRepository.save(bucket);
			logger.info("Bucket added successfully");
		}
	}

	@Transactional
	public void updateRequisitionToVendor(List<ObjectNode> list) {
		for (ObjectNode obj : list) {
			logger.debug("Update requisition to vendor: " + obj.toString());
			Optional<VendorRequisitionBucket> ur = vendorRequisitionBucketRepository
					.findById(Long.parseLong(obj.get("id").asText()));
			if (!ur.isPresent()) {
				logger.error("Vendor requisition bucket not found");
			}
			VendorRequisitionBucket bucket = ur.get();
			if (obj.get("stages") != null) {
				bucket.setStages(obj.get("stages").asText());
			}
			if (obj.get("notes") != null) {
				bucket.setNotes(obj.get("notes").asText());
			}
			if (obj.get("status") != null) {
				bucket.setStatus(obj.get("status").asText());
			}

			if (obj.get("user") != null) {
				bucket.setCreatedBy(obj.get("user").asText());
				bucket.setUpdatedBy(obj.get("user").asText());
			} else {
				bucket.setCreatedBy(Constants.SYSTEM_ACCOUNT);
				bucket.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			}
			Instant now = Instant.now();
			bucket.setCreatedOn(now);
			bucket.setUpdatedOn(now);
			bucket = vendorRequisitionBucketRepository.save(bucket);
			logger.info("Bucket update successfully");
		}
	}

//	public boolean approveRequisition(ObjectNode obj) throws JSONException {
//		logger.info("Getting requisition by id: " + obj);
//
//		try {
//			if (obj.get("requisitionId") == null) {
//				logger.error("Requision id not found. Cannot approve requisition.");
//				return false;
//			}
//
////			if (obj.get("roleName") == null) {
////				logger.error("Role not found. Cannot approve requisition.");
////				return false;
////			}
//
//			Optional<Requisition> req = requisitionRepository.findById(obj.get("requisitionId").asLong());
//			if (!req.isPresent()) {
//				logger.error("Requision not found. Cannot approve requisition.");
//				return false;
//			}
//
//			Requisition requisition = req.get();
//			
//			Rules rule = rulesService.getRulesByName(Constants.RULE_APPROVE_REQUISITION); 
//			Roles role = rolesService.getRolesByName(obj.get("roleName").asText());
//			// TO DO. Find rule assigned to a role in roles_rules_link table 
//			JSONObject jsonObject = new JSONObject(rule.getRule());
//
//			int price = 0;
//			if (requisition.getTotalPrice() != null) {
//				price = requisition.getTotalPrice().intValue();
//			}
//
//			int minRulePrice = 0;
//			int maxRulePrice = 0;
//
//			try {
//				minRulePrice = jsonObject.getInt("min");
//			} catch (Exception e) {
//				logger.error("Minimum price rule not found. Cannot approve requisiont. Exception: ", e);
//				return false;
//			}
//
//			if (jsonObject.get("max") != null) {
//				try {
//					maxRulePrice = jsonObject.getInt("max");
//				} catch (Exception e) {
//					logger.error("Incorrect maximum price rule. Cannot approve requisiont. Exception: ", e);
//					return false;
//				}
//			}
//
//			boolean isRuleApplied = false;
//			if (price >= minRulePrice && jsonObject.get("max") == null) {
//				requisition.setStatus(Constants.PROGRESS_STAGE_APPROVED);
//				isRuleApplied = true;
//			}
//
//			if (price >= minRulePrice && jsonObject.get("max") != null && price <= maxRulePrice) {
//				requisition.setStatus(Constants.PROGRESS_STAGE_APPROVED);
//				isRuleApplied = true;
//			}
//
//			if (isRuleApplied) {
//				requisition = requisitionRepository.save(requisition);
//				saveRequisitionActivity(requisition);
//				return true;
//			} else {
//				logger.warn("Approve requisition failed. No rule applied");
//				return false;
//			}
//
//		} catch (Exception e) {
//			logger.error("Approve requisition failed. Exception: ", e);
//			return false;
//		}
//
//	}

	public boolean approveRequisition(ObjectNode obj){
		logger.info("Getting requisition by id: " + obj);

		try {
			if (obj.get("requisitionId") == null) {
				logger.error("Requision id not found. Cannot approve requisition.");
				return false;
			}

			Optional<Requisition> req = requisitionRepository.findById(obj.get("requisitionId").asLong());
			if (!req.isPresent()) {
				logger.error("Requision not found. Cannot approve requisition.");
				return false;
			}

			Requisition requisition = req.get();
			requisition.setStatus(Constants.PROGRESS_STAGE_APPROVED);
			requisition = requisitionRepository.save(requisition);
			saveRequisitionActivity(requisition);
			logger.info("Requisition approved successfully");
			return true;

		} catch (Exception e) {
			logger.error("Approve requisition failed. Exception: ", e);
			return false;
		}

	}
 
	@Transactional
	private void saveFile(MultipartFile[] files, Requisition requisition, Instant now, String identifier)
			throws IOException, JSONException {
		AmazonS3 s3client = AmazonS3ClientBuilder
		  .standard()
		  .withCredentials(new AWSStaticCredentialsProvider(xformAwsS3Config.getAwsCredentials()))
		  .withRegion(Regions.fromName(xformAwsS3Config.getRegion()))
		  .build();
		for (MultipartFile file : files) {
			
			byte[] bytes = file.getBytes();
			Map<String,String> nameMap = getFileName(file);
			
			if(!org.apache.commons.lang3.StringUtils.isBlank(Constants.IS_LOCAL_FILE_STORE)
					&& "Y".equalsIgnoreCase(Constants.IS_LOCAL_FILE_STORE)) {
				File localFile = new File(Constants.LOCAL_FILE_PATH);
				logger.info("Saving requistion file to local: "+getFileName(file));
				if (!localFile.exists()) {
					localFile.mkdirs();
				}
				String absolutePath = localFile.getAbsolutePath() + File.separatorChar + nameMap.get("fileName");
				Path path = Paths.get(absolutePath);
				Files.write(path, bytes);
				saveDocument(requisition, absolutePath, null, null, now, file.getSize(), nameMap, identifier);
			}
			
			if(!org.apache.commons.lang3.StringUtils.isBlank(Constants.IS_AWS_FIEL_STORE)
					&& "Y".equalsIgnoreCase(Constants.IS_AWS_FIEL_STORE)) {
				logger.info("Saving requistion file to AWS: "+getFileName(file));
				String awsFileUrl = uploadFileToAwsS3(s3client, Constants.REQUISITION_BUCKET, Constants.REQUISITION_DIRECTORY,  nameMap, file);
				saveDocument(requisition, null, Constants.REQUISITION_BUCKET, awsFileUrl, now, file.getSize(), nameMap, identifier);
			}
				
//				requisition.setExtraBudgetoryFile(bytes);
//				logger.info("Extra budgetory file saved successfully");
		}
		s3client.shutdown();
	}

	@Transactional
	private void saveDocument(Requisition requisition, String localFilePath, String s3Bucket, String s3Url, Instant now, long fileSize, Map<String, String> nameMap,
			String identifier) {
		Document document = new Document();
		document.setFileName(nameMap.get("fileName"));
		document.setFileExt(nameMap.get("ext"));
		document.setFileType(nameMap.get("ext").toUpperCase());
		document.setFileSize(fileSize);
		
		if(!org.apache.commons.lang3.StringUtils.isBlank(localFilePath)) {
			document.setLocalFilePath(localFilePath);
		}
		if(!org.apache.commons.lang3.StringUtils.isBlank(s3Bucket)) {
			document.sets3Bucket(s3Bucket);
		}
		if(!org.apache.commons.lang3.StringUtils.isBlank(s3Url)) {
			document.sets3Url(s3Url);
		}
//		document.setStorageLocation(Constants.FILE_STORAGE_LOCATION_LOCAL);
		document.setSourceOfOrigin(this.getClass().getSimpleName());
		document.setSourceId(requisition.getId());
		document.setIdentifier(identifier);
		document.setCreatedBy(requisition.getCreatedBy());
		document.updatedBy(requisition.getCreatedBy());
		document.setCreatedOn(now);
		document.setUpdatedOn(now);
		document = documentService.saveDocument(document);
		requisition.getDocumentList().add(document);
	}

	@Transactional
	public void deleteRequisition(Long requisitionId) {
		logger.info("Deleting requisition");
		Requisition req = getRequisition(requisitionId);
		Instant now = Instant.now();
		if (req != null) {
			req.setStatus(Constants.STATUS_DELETED);
			req.setUpdatedOn(now);
			saveRequisitionActivity(req);
		}

		Map<String, String> reqLineItemMap = new HashMap<>();
		reqLineItemMap.put("requisitionId", String.valueOf(requisitionId));
		List<RequisitionLineItem> reqLineItemList = requisitionLineItemService
				.searchRequisitionLineItem(reqLineItemMap);
		for (RequisitionLineItem reqLineItem : reqLineItemList) {

			RequisitionLineItemActivity reqLiAct = new RequisitionLineItemActivity();
			BeanUtils.copyProperties(reqLineItem, reqLiAct);
			reqLiAct.setRequisitionLineItemId(reqLineItem.getId());
			reqLiAct.status(Constants.STATUS_DELETED);
			reqLiAct.setUpdatedOn(now);
			reqLiAct = requisitionLineItemActivityService.addRequisitionLineItemActivity(reqLiAct);
			requisitionLineItemService.deleteRequisitionLineItem(reqLineItem.getId());

		}
		requisitionRepository.deleteById(requisitionId);
		logger.info(" Requisition deleted successfully");
	}

	
	private Map<String, String> getFileName(MultipartFile file) {
		Map<String,String> nameMap = new HashMap<>();
		String orgFileName = StringUtils.cleanPath(file.getOriginalFilename());
		String ext = "";
		if (orgFileName.lastIndexOf(".") != -1) {
			ext = orgFileName.substring(orgFileName.lastIndexOf(".") + 1);
		}
		String fileName = orgFileName;
		if (orgFileName.lastIndexOf(".") != -1) {
			fileName = orgFileName.substring(0, orgFileName.lastIndexOf("."));
		}
		fileName = fileName.toLowerCase().replaceAll(" ", "-") + "_" + System.currentTimeMillis() + "." + ext;
		nameMap.put("fileName", fileName);
		nameMap.put("ext", ext);
		return nameMap; 
	}
	
	private String uploadFileToAwsS3(AmazonS3 s3client, String bucket, String directory, Map<String,String> nameMap, MultipartFile file) throws IOException{
		byte[] buffer = new byte[file.getInputStream().available()];
		file.getInputStream().read(buffer);
		File tempFile = File.createTempFile(nameMap.get("fileName"), "");
		try (OutputStream outStream = new FileOutputStream(tempFile)) {
		    outStream.write(buffer);
		}
		String key = directory+"/"+nameMap.get("fileName");
		PutObjectResult res = s3client.putObject(
				bucket, 
				key,
				tempFile);
		tempFile.deleteOnExit();
		
		return s3client.getUrl(bucket, key).toExternalForm();
		
	}
}