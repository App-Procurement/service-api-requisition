package com.synectiks.procurement.business.service;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Buyer;
import com.synectiks.procurement.domain.BuyerRequisitionLink;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.repository.BuyerRequisitionLinkRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@Service
public class BuyerRequisitionLinkService {

	private static final Logger logger = LoggerFactory.getLogger(BuyerRequisitionLinkService.class);

	@Autowired
	private BuyerRequisitionLinkRepository buyerRequisitionLinkRepository;

	@Autowired
	RequisitionService requisitionService;
	@Autowired
	BuyerService buyerService;

	public List<BuyerRequisitionLink> addBuyerRequisitionLink(ObjectNode obj, Integer requisitionId)
			throws NegativeIdException, ParseException, IOException {

		List<BuyerRequisitionLink> brlList = new ArrayList<BuyerRequisitionLink>();

		Map<String, String> rm = new HashMap<String, String>();

		rm.put("id", requisitionId.toString());

		List<Requisition> rList = requisitionService.searchRequisition(rm);

		for (JsonNode buyerId : obj.get("buyersId")) {
			BuyerRequisitionLink buyerRequisitionLink = new BuyerRequisitionLink();

			if (rList.get(0) != null) {
				buyerRequisitionLink.setRequisition(rList.get(0));
			}

			Map<String, String> bm = new HashMap<String, String>();
			bm.put("id", buyerId.asText());

			List<Buyer> bList = buyerService.searchBuyer(bm);

			if (bList.get(0) != null) {
				buyerRequisitionLink.setBuyer(bList.get(0));
			}

			Instant now = Instant.now();
			buyerRequisitionLink.setCreatedOn(now);
			buyerRequisitionLink.setUpdatedOn(now);
			buyerRequisitionLink = buyerRequisitionLinkRepository.save(buyerRequisitionLink);
			logger.info("buyerRequisitionLink added successfully: " + buyerRequisitionLink.toString());
			brlList.add(buyerRequisitionLink);
		}

		return brlList;
	}

	public List<BuyerRequisitionLink> searchBuyerRequisitionLink(@RequestParam Map<String, String> requestObj,
			Integer requisitionId) throws NegativeIdException, ParseException {
		BuyerRequisitionLink buyerRequisitionLink = new BuyerRequisitionLink();
		boolean isFilter = false;

		Map<String, String> rm = new HashMap<String, String>();
		rm.put("id", requisitionId.toString());
		List<Requisition> rList = requisitionService.searchRequisition(rm);

		if (rList.get(0) != null) {
			buyerRequisitionLink.setRequisition(rList.get(0));
		}

		if (requestObj.get("id") != null) {

			if (Long.parseLong(requestObj.get("id")) < 0) {
				throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
			}

			buyerRequisitionLink.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (requestObj.get("firstName") != null) {
			buyerRequisitionLink.setRequisition(null);
			isFilter = true;
		}
		if (requestObj.get("firstName") != null) {
			buyerRequisitionLink.setBuyer(null);
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant inst = Instant.parse(requestObj.get("createdOn"));
			buyerRequisitionLink.setCreatedOn(inst);
			isFilter = true;
		}

		if (requestObj.get("createdBy") != null) {
			buyerRequisitionLink.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant inst = Instant.parse(requestObj.get("updatedOn"));
			buyerRequisitionLink.setUpdatedOn(inst);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			buyerRequisitionLink.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}
		List<BuyerRequisitionLink> list = null;
		if (isFilter) {
			list = this.buyerRequisitionLinkRepository.findAll(Example.of(buyerRequisitionLink),
					Sort.by(Direction.DESC, "id"));
		} else {
			list = this.buyerRequisitionLinkRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		logger.info("buyerRequisitionLink search completed. Total records: " + list.size());

		return list;
	}

	public boolean deleteBuyerRequisitionLink(Long id)
			throws IdNotFoundException, NegativeIdException, DataNotFoundException {

		if (id == null) {
			logger.error("buyer Requisition Link could not be deleted. buyerRequisitionLink id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}
		if (id < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}

		Optional<BuyerRequisitionLink> buyerRequisitionLink = buyerRequisitionLinkRepository.findById(id);

		if (!buyerRequisitionLink.isPresent()) {
			logger.error("buyerRequisitionLink could not be deleted. buyerRequisitionLink not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}

		buyerRequisitionLinkRepository.deleteById(id);
		logger.info("buyerRequisitionLink deleted successfully");
		return true;
	}
}
