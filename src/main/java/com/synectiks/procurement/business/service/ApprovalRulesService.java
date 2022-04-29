package com.synectiks.procurement.business.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.ApprovalRules;
import com.synectiks.procurement.repository.ApprovalRulesRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@Service
public class ApprovalRulesService {

	private static final Logger logger = LoggerFactory.getLogger(ApprovalRulesService.class);

	@Autowired
	private ApprovalRulesRepository approvalRulesRepository;

//	public approvalRules getapprovalRules(Long id) {
//		logger.info("Getting approvalRules by id: " + id);
//		Optional<approvalRules> buy = approvalRulesRepository.findById(id);
//		if (buy.isPresent()) {
//			logger.info("approvalRules: " + buy.get().toString());
//			return buy.get();
//		}
//		logger.warn("approvalRules not found");
//		return null;
//	}

	public ApprovalRules addApprovalRules(ObjectNode obj) {
		ApprovalRules approvalRules = new ApprovalRules();

		if (obj.get("role") != null) {
			approvalRules.setRole(obj.get("role").asText());
		}
		if (obj.get("minLimit") != null) {
			approvalRules.setMinLimit(obj.get("minLimit").asInt());
		}
		if (obj.get("maxLimit") != null) {
			approvalRules.setMaxLimit(obj.get("maxLimit").asInt());
		}
		if (obj.get("status") != null) {
			approvalRules.setStatus(obj.get("status").asText());
		}
		if (obj.get("user") != null) {
			approvalRules.setCreatedBy(obj.get("user").asText());
			approvalRules.setUpdatedBy(obj.get("user").asText());
		} else {
			approvalRules.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			approvalRules.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		approvalRules.setCreatedOn(now);
		approvalRules.setUpdatedOn(now);
		approvalRules = approvalRulesRepository.save(approvalRules);
		logger.info("approvalRules added successfully: " + approvalRules.toString());
		return approvalRules;
	}

	public ApprovalRules updateApprovalRules(ObjectNode obj) throws NegativeIdException, IdNotFoundException, DataNotFoundException{
		
		if(org.apache.commons.lang3.StringUtils.isBlank(obj.get("id").asText())) {
			logger.error("approvalRules could not be updated. approvalRules id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}
		
		Long buy = Long.parseLong(obj.get("id").asText());
		if( buy < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}
		Optional<ApprovalRules> bu = approvalRulesRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!bu.isPresent()) {
			logger.error("approvalRules not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}
		
		ApprovalRules approvalRules = bu.get();

		if (obj.get("role") != null) {
			approvalRules.setRole(obj.get("role").asText());
		}
		if (obj.get("minLimit") != null) {
			approvalRules.setMinLimit(obj.get("minLimit").asInt());
		}
		if (obj.get("maxLimit") != null) {
			approvalRules.setMaxLimit(obj.get("maxLimit").asInt());
		}
		if (obj.get("status") != null) {
			approvalRules.setStatus(obj.get("status").asText());
		}
		else {
			approvalRules.setStatus(Constants.STATUS_DEACTIVE);
		}
		if (obj.get("user") != null) {
			approvalRules.setUpdatedBy(obj.get("user").asText());
		} else {
			approvalRules.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		approvalRules.setUpdatedOn(Instant.now());
		approvalRules = approvalRulesRepository.save(approvalRules);
		logger.info("Updating approvalRules completed" + approvalRules.toString());
		return approvalRules;
	}

	public List<ApprovalRules> searchApprovalRules(@RequestParam Map<String, String> requestObj) throws NegativeIdException {
		ApprovalRules approvalRules = new ApprovalRules();
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			
			Long buyId =Long.parseLong(requestObj.get("id"));
			
			if(buyId < 0) {
				throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
			}
			approvalRules.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (requestObj.get("role") != null) {
			approvalRules.setRole(requestObj.get("role"));
			isFilter = true;
		}
		if (requestObj.get("minLimit") != null) {
			approvalRules.setMinLimit(Integer.parseInt(requestObj.get("minLimit")) );
			isFilter = true;
		}
		if (requestObj.get("maxLimit") != null) {
			approvalRules.setMaxLimit(Integer.parseInt( requestObj.get("maxLimit")));
			isFilter = true;
		}
		if (requestObj.get("status") != null) {
			approvalRules.setStatus(requestObj.get("status"));
			isFilter = true;
		}
		
		if (requestObj.get("createdOn") != null) {
			Instant inst = Instant.parse(requestObj.get("createdOn"));
			approvalRules.setCreatedOn(inst);
			isFilter = true;
		}

		if (requestObj.get("createdBy") != null) {
			approvalRules.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant inst = Instant.parse(requestObj.get("updatedOn"));
			approvalRules.setUpdatedOn(inst);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			approvalRules.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}
		List<ApprovalRules> list = null;
		if (isFilter) {
			list = this.approvalRulesRepository.findAll(Example.of(approvalRules), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.approvalRulesRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		logger.info("approvalRules search completed. Total records: " + list.size());

		return list;
	}

	public void deleteApprovalRules(Long id) {
		approvalRulesRepository.deleteById(id);
		logger.info("approvalRules deleted successfully");
	}
}
