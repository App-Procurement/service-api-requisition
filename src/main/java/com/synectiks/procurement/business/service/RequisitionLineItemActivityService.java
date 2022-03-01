package com.synectiks.procurement.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synectiks.procurement.domain.RequisitionLineItemActivity;
import com.synectiks.procurement.repository.RequisitionLineItemActivityRepository;

@Service
public class RequisitionLineItemActivityService {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionLineItemActivityService.class);

//	@Autowired
//	private RequisitionLineItemService requisitionLineItemService;

	@Autowired
	private RequisitionLineItemActivityRepository requisitionLineItemActivityRepository;

//	@Autowired
//	private RequisitionService requisitionService;

	public RequisitionLineItemActivity addRequisitionLineItemActivity(RequisitionLineItemActivity obj) {
		logger.info("Add Requisition line item activity");
		obj = requisitionLineItemActivityRepository.save(obj);
		return obj;
	}

}
