package com.synectiks.procurement.business.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.synectiks.procurement.domain.RequisitionActivity;
import com.synectiks.procurement.repository.RequisitionActivityRepository;

@Service
public class RequisitionActivityService {

	private static final Logger logger = LoggerFactory.getLogger(RequisitionActivityService.class);

	@Autowired
	private RequisitionActivityRepository requisitionActivityRepository;

	public RequisitionActivity addRequisitionActivity(RequisitionActivity requisitionActivity) {
		logger.info("Adding requisition activity");
		requisitionActivity = requisitionActivityRepository.save(requisitionActivity);
		logger.info("Requisition activity added successfully");
		return requisitionActivity;
	}

	public List<RequisitionActivity> searchRequisitionActivity(Map<String, String> requestObj) {
		logger.info("Request to search requisition activity on given filter criteria");
		RequisitionActivity requisitionActivity = new RequisitionActivity();
		boolean isFilter = false;

		if (requestObj.get("id") != null) {
			requisitionActivity.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (requestObj.get("requisitionNo") != null) {
			requisitionActivity.setRequisitionNo(requestObj.get("requisitionNo"));
			isFilter = true;
		}

		if (requestObj.get("status") != null) {
			requisitionActivity.setStatus(requestObj.get("status"));
			isFilter = true;
		}

		if (requestObj.get("progressStage") != null) {
			requisitionActivity.setProgressStage(requestObj.get("progressStage"));
			isFilter = true;
		}

		if (requestObj.get("financialYear") != null) {
			// requisition.setFinancialYear(requestObj.get("progressStage"));
			isFilter = true;
		}
		if (requestObj.get("type") != null) {
			requisitionActivity.setType(requestObj.get("type"));
			isFilter = true;
		}
		if (requestObj.get("totalPrice") != null) {
			requisitionActivity.setTotalPrice(Integer.parseInt(requestObj.get("totalPrice")));
			isFilter = true;
		}
		if (requestObj.get("notes") != null) {
			requisitionActivity.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant instant = Instant.parse(requestObj.get("createdOn"));
			requisitionActivity.setCreatedOn(instant);
			isFilter = true;
		}
		if (requestObj.get("createdBy") != null) {
			requisitionActivity.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant instant = Instant.parse(requestObj.get("updatedOn"));
			requisitionActivity.setUpdatedOn(instant);
			isFilter = true;
		}
	    if (requestObj.get("updatedBy") != null) {
	    	requisitionActivity.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}

		List<RequisitionActivity> list = null;
		if (isFilter) {
			list = this.requisitionActivityRepository.findAll(Example.of(requisitionActivity),
					Sort.by(Direction.DESC, "id"));
		} else {
			list = this.requisitionActivityRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		logger.info("Requistion activity search completed. Total records: " + list.size());
		return list;
	}

	public void deletePost(Long id) {
		requisitionActivityRepository.deleteById(id);
		logger.info("Requistion activity deleted successfully");
	}

	public List<RequisitionActivity> getAllRequisition() {
		List<RequisitionActivity> list = requisitionActivityRepository.findAll(Sort.by(Direction.ASC, "id"));
		return list;
	}
}
