package com.synectiks.procurement.business.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.RequisitionLineItemActivity;
import com.synectiks.procurement.repository.RequisitionLineItemActivityRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.repository.RequisitionRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@Service
public class RequisitionLineItemService {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionLineItemService.class);

	@Autowired
	private RequisitionLineItemRepository requisitionLineItemRepository;

	@Autowired
	private RequisitionLineItemActivityService requisitionLineItemActivityService;

	@Autowired
	private RequisitionRepository requisitionRepository;

	@Autowired
	private RequisitionLineItemActivityRepository requisitionLineItemActivityRepository;

	@Autowired
	private RequisitionService requisitionService;

	public RequisitionLineItem addRequisitionLineItem(RequisitionLineItem obj) {
		logger.info("Add requisition line item");
		obj = requisitionLineItemRepository.save(obj);

		if (obj != null) {
			logger.info("Saving requisition line item acitivity");
			RequisitionLineItemActivity requisitionLineItemActivity = new RequisitionLineItemActivity();
			BeanUtils.copyProperties(obj, requisitionLineItemActivity);
			requisitionLineItemActivity.setRequisitionLineItemId(obj.getId());
			requisitionLineItemActivity = requisitionLineItemActivityService
					.addRequisitionLineItemActivity(requisitionLineItemActivity);
			logger.info("Requisition line item acitivity saved successfully");
		}
		return obj;
	}

	@Transactional
	public RequisitionLineItem addRequisitionLineItem(String obj1, MultipartFile[] requisitionLineItemFile)
			throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = (ObjectNode) mapper.readTree(obj1);

		RequisitionLineItem requisitionLineItem = new RequisitionLineItem();

		Optional<Requisition> oReq = requisitionRepository.findById(Long.parseLong(obj.get("requisitionId").asText()));

		Requisition requisition = oReq.get();
		if (!oReq.isPresent()) {
			logger.error("Requisition line item cannot be added. Requisition missing");
			return null;
		}
		requisitionLineItem.setRequisition(oReq.get());

		if (obj.get("status") != null) {
			requisitionLineItem.setStatus(obj.get("status").asText());
		}

		if (obj.get("progressStage") != null) {
			requisitionLineItem.setProgressStage(obj.get("progressStage").asText());
		}

		if (obj.get("itemDescription") != null) {
			requisitionLineItem.setItemDescription(obj.get("itemDescription").asText());
		}

		if (obj.get("orderQuantity") != null) {
			requisitionLineItem.setOrderQuantity(obj.get("orderQuantity").asInt());
		}

		if (obj.get("ratePerItem") != null) {
			requisitionLineItem.setRatePerItem(obj.get("ratePerItem").asInt());
		}

		if (obj.get("price") != null) {
			requisitionLineItem.setPrice(obj.get("price").asInt());
		}

		if (obj.get("priority") != null) {
			requisitionLineItem.setPriority(obj.get("priority").asText());
		}

		if (obj.get("notes") != null) {
			requisitionLineItem.setNotes(obj.get("notes").asText());
		}
		if (obj.get("status") != null) {
			requisitionLineItem.setStatus(obj.get("status").asText());
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if (obj.get("dueDate") != null) {
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			requisitionLineItem.setDueDate(localDate);
		} else {
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);

			LocalDate datew = LocalDate.parse(date.toString());
			LocalDate localDate = datew.plusDays(Constants.DEFAULT_DUE_DAYS);
			requisitionLineItem.setDueDate(localDate);
		}
		if (obj.get("user") != null) {
			requisitionLineItem.setCreatedBy(obj.get("user").asText());
			requisitionLineItem.setUpdatedBy(obj.get("user").asText());
		} else {
			requisitionLineItem.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			requisitionLineItem.setUpdatedBy(Constants.SYSTEM_ACCOUNT);

		}
		Instant now = Instant.now();
		requisitionLineItem.setCreatedOn(now);
		requisitionLineItem.setUpdatedOn(now);
		logger.debug("RequisitionLineItem object: " + requisitionLineItem.toString());
		requisitionLineItem = requisitionLineItemRepository.save(requisitionLineItem);
		logger.info("Requisition line item added successfully");

		if (requisitionLineItem != null) {
			logger.info("Adding requisition line item acitivity");
			RequisitionLineItemActivity requisitionLineItemActivity = new RequisitionLineItemActivity();
			BeanUtils.copyProperties(requisitionLineItem, requisitionLineItemActivity);
			requisitionLineItemActivity.setRequisitionLineItemId(requisitionLineItem.getId());
			requisitionLineItemActivity = requisitionLineItemActivityService
					.addRequisitionLineItemActivity(requisitionLineItemActivity);
			logger.info("Requisition line item acitivity added successfully");
		}

		List<RequisitionLineItem> liteItemList = requisitionService.getLineItemFromFile(requisitionLineItemFile);
		liteItemList.add(requisitionLineItem);

		int totalAmt = 0;
		for (RequisitionLineItem rqLnItm : liteItemList) {
			int amt = rqLnItm.getOrderQuantity() * rqLnItm.getRatePerItem();
			totalAmt = totalAmt + amt;
		}

		requisition.setTotalPrice(totalAmt);
		requisitionRepository.save(requisition);

		requisitionService.saveRequisitionLineItem(requisition, liteItemList);

		return requisitionLineItem;
	}

	@Transactional
	public RequisitionLineItem updateRequisitionLineItem(String obj1, MultipartFile[] requisitionLineItemFile)
			throws IOException, IdNotFoundException, NegativeIdException, DataNotFoundException {
		logger.info("Update Requisition line item");

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = (ObjectNode) mapper.readTree(obj1);

		if (org.apache.commons.lang3.StringUtils.isBlank(obj.get("id").asText())) {
			logger.error("Requisition line item could not be updated. Requisition line item id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}

		Long reqlId = Long.parseLong(obj.get("id").asText());
		if (reqlId < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}

		Optional<RequisitionLineItem> orqli = requisitionLineItemRepository
				.findById(Long.parseLong(obj.get("id").asText()));
		if (!orqli.isPresent()) {
			logger.error("Requisition line item  could not be updated. Requisition line item  not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}

		RequisitionLineItem requisitionLineItem = orqli.get();

		Requisition requisition = requisitionLineItem.getRequisition();

		if (requisition == null) {
			logger.error("Requisition line item cannot be updated. Requisition missing");
			return null;
		}

		if (obj.get("status") != null) {
			requisitionLineItem.setStatus(obj.get("status").asText());
		}
		else {
			requisitionLineItem.setStatus(Constants.STATUS_DEACTIVE);
		}

		if (obj.get("progressStage") != null) {
			requisitionLineItem.setProgressStage(obj.get("progressStage").asText());
		}

		if (obj.get("itemDescription") != null) {
			requisitionLineItem.setItemDescription(obj.get("itemDescription").asText());
		}

		if (obj.get("ratePerItem") != null) {
			requisitionLineItem.setRatePerItem(obj.get("ratePerItem").asInt());
		}

		if (obj.get("orderQuantity") != null) {
			requisitionLineItem.setOrderQuantity(obj.get("orderQuantity").asInt());
		}

		if (obj.get("price") != null) {
			requisitionLineItem.setPrice(obj.get("price").asInt());
		}

		if (obj.get("priority") != null) {
			requisitionLineItem.setPriority(obj.get("priority").asText());
		}

		if (obj.get("notes") != null) {
			requisitionLineItem.setNotes(obj.get("notes").asText());
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if (obj.get("dueDate") != null) {
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			requisitionLineItem.setDueDate(localDate);
		}

		if (obj.get("user") != null) {
			requisitionLineItem.setUpdatedBy(obj.get("user").asText());
		} else {
			requisitionLineItem.setUpdatedBy(Constants.SYSTEM_ACCOUNT);

		}

		requisitionLineItem.setUpdatedOn(Instant.now());
		logger.debug("RequisitionLineItem object: " + requisitionLineItem.toString());
		requisitionLineItem = requisitionLineItemRepository.save(requisitionLineItem);
		logger.info("Requisition line item added successfully");

		if (requisitionLineItem != null) {
			logger.info("Adding requisition line item acitivity");
			RequisitionLineItemActivity requisitionLineItemActivity = new RequisitionLineItemActivity();
			BeanUtils.copyProperties(requisitionLineItem, requisitionLineItemActivity);
			requisitionLineItemActivity.setRequisitionLineItemId(requisitionLineItem.getId());
			requisitionLineItemActivity = requisitionLineItemActivityService
					.addRequisitionLineItemActivity(requisitionLineItemActivity);
			logger.info("Requisition line item acitivity added successfully");
		}

		List<RequisitionLineItem> liteItemList = requisitionService
				.getLineItemFromFileForUpdate(requisitionLineItemFile);
		liteItemList.add(requisitionLineItem);

		int totalAmt = 0;
		for (RequisitionLineItem rqLnItm : liteItemList) {
			int amt = rqLnItm.getOrderQuantity() * rqLnItm.getRatePerItem();
			totalAmt = totalAmt + amt;
		}

		requisition.setTotalPrice(totalAmt);
		requisitionRepository.save(requisition);

		requisitionService.saveRequisitionLineItem(requisition, liteItemList);

		return requisitionLineItem;

	}

	public List<RequisitionLineItem> searchRequisitionLineItem(Map<String, String> requestObj)
			throws NegativeIdException {
		logger.info("Request to get requisition line item on given filter criteria");
		RequisitionLineItem requisitionLineItem = new RequisitionLineItem();
		boolean isFilter = false;

		if (requestObj.get("id") != null) {
			if (Long.parseLong(requestObj.get("id")) < 0) {
				throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
			}

			requisitionLineItem.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (requestObj.get("progressStage") != null) {
			requisitionLineItem.setProgressStage(requestObj.get("progressStage"));
			isFilter = true;
		}
		if (requestObj.get("itemDescription") != null) {
			requisitionLineItem.setItemDescription(requestObj.get("itemDescription"));
			isFilter = true;
		}
		if (requestObj.get("orderQuantity") != null) {
			requisitionLineItem.setOrderQuantity(Integer.valueOf(requestObj.get("orderQuantity")));
			isFilter = true;
		}
		if (requestObj.get("price") != null) {
			requisitionLineItem.setPrice(Integer.valueOf(requestObj.get("price")));
			isFilter = true;
		}
		if (requestObj.get("priority") != null) {
			requisitionLineItem.setPriority(requestObj.get("priority"));
			isFilter = true;
		}
		if (requestObj.get("notes") != null) {
			requisitionLineItem.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant instant = Instant.parse(requestObj.get("createdOn"));
			requisitionLineItem.setCreatedOn(instant);
			isFilter = true;
		}
		if (requestObj.get("createdBy") != null) {
			requisitionLineItem.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant instant = Instant.parse(requestObj.get("updatedOn"));
			requisitionLineItem.setUpdatedOn(instant);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			requisitionLineItem.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}

		if (requestObj.get("requisitionId") != null) {
			Optional<Requisition> oReq = requisitionRepository
					.findById(Long.parseLong(requestObj.get("requisitionId")));
			if (oReq.isPresent()) {
				requisitionLineItem.setRequisition(oReq.get());
			}
			isFilter = true;
		}

		List<RequisitionLineItem> list = null;
		if (isFilter) {
			list = this.requisitionLineItemRepository.findAll(Example.of(requisitionLineItem),
					Sort.by(Direction.DESC, "id"));
		} else {
			list = this.requisitionLineItemRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		for (RequisitionLineItem qou : list) {
			RequisitionLineItemActivity ca = new RequisitionLineItemActivity();
			ca.setRequisitionLineItemId(qou.getId());
			List<RequisitionLineItemActivity> caList = requisitionLineItemActivityRepository.findAll(Example.of(ca));
			qou.setActivityList(caList);
		}

		logger.info("Requisition line item order search completed. Total records: " + list.size());
		return list;
	}

	public void deleteRequisitionLineItem(Long id)
			throws IdNotFoundException, NegativeIdException, DataNotFoundException {
		if (id == null) {
			logger.error("Requisition line item could not be deleted. Vendor id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}
		if (id < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}

		Optional<RequisitionLineItem> reli = requisitionLineItemRepository.findById(id);

		if (!reli.isPresent()) {
			logger.error("Requisition line item could not be deleted. Requisition line item not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}

		requisitionLineItemRepository.deleteById(id);
	}

	public RequisitionLineItem getRequisitionLineItem(Long id) {
		logger.info("Getting requisition line item by id: " + id);
		Optional<RequisitionLineItem> ovn = requisitionLineItemRepository.findById(id);
		if (ovn.isPresent()) {
			logger.info("RequisitionLineItem: " + ovn.get().toString());
			return ovn.get();
		}
		logger.warn("Requisition line item not found");
		return null;
	}
}
