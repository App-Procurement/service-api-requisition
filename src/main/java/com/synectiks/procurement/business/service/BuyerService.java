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
import com.synectiks.procurement.domain.Buyer;
import com.synectiks.procurement.repository.BuyerRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@Service
public class BuyerService {

	private static final Logger logger = LoggerFactory.getLogger(BuyerService.class);

	@Autowired
	private BuyerRepository buyerRepository;

//	public Buyer getBuyer(Long id) {
//		logger.info("Getting buyer by id: " + id);
//		Optional<Buyer> buy = buyerRepository.findById(id);
//		if (buy.isPresent()) {
//			logger.info("Buyer: " + buy.get().toString());
//			return buy.get();
//		}
//		logger.warn("Buyer not found");
//		return null;
//	}

	public Buyer addBuyer(ObjectNode obj) {
		Buyer buyer = new Buyer();

		if (obj.get("firstName") != null) {
			buyer.setFirstName(obj.get("firstName").asText());
		}
		if (obj.get("middleName") != null) {
			buyer.setMiddleName(obj.get("middleName").asText());
		}
		if (obj.get("lastName") != null) {
			buyer.setLastName(obj.get("lastName").asText());
		}
		if (obj.get("phoneNumber") != null) {
			buyer.setPhoneNumber(obj.get("phoneNumber").asText());
		}
		if (obj.get("email") != null) {
			buyer.setEmail(obj.get("email").asText());
		}
		if (obj.get("address") != null) {
			buyer.setAddress(obj.get("address").asText());
		}
		if (obj.get("zipCode") != null) {
			buyer.setZipCode(obj.get("zipCode").asText());
		}
		if (obj.get("status") != null) {
			buyer.setStatus(obj.get("status").asText());
		}
		if (obj.get("user") != null) {
			buyer.setCreatedBy(obj.get("user").asText());
			buyer.setUpdatedBy(obj.get("user").asText());
		} else {
			buyer.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			buyer.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		buyer.setCreatedOn(now);
		buyer.setUpdatedOn(now);
		buyer = buyerRepository.save(buyer);
		logger.info("Buyer added successfully: " + buyer.toString());
		return buyer;
	}

	public Buyer updateBuyer(ObjectNode obj) throws NegativeIdException, IdNotFoundException, DataNotFoundException{
		
		if(org.apache.commons.lang3.StringUtils.isBlank(obj.get("id").asText())) {
			logger.error("Buyer could not be updated. Buyer id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}
		
		Long buy = Long.parseLong(obj.get("id").asText());
		if( buy < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}
		Optional<Buyer> bu = buyerRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!bu.isPresent()) {
			logger.error("Buyer not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}
		
		Buyer buyer = bu.get();

		if (obj.get("firstName") != null) {
			buyer.setFirstName(obj.get("firstName").asText());
		}
		if (obj.get("middleName") != null) {
			buyer.setMiddleName(obj.get("middleName").asText());
		}
		if (obj.get("lastName") != null) {
			buyer.setLastName(obj.get("lastName").asText());
		}
		if (obj.get("phoneNumber") != null) {
			buyer.setPhoneNumber(obj.get("phoneNumber").asText());
		}
		if (obj.get("email") != null) {
			buyer.setEmail(obj.get("email").asText());
		}
		if (obj.get("address") != null) {
			buyer.setAddress(obj.get("address").asText());
		}
		if (obj.get("zipCode") != null) {
			buyer.setZipCode(obj.get("zipCode").asText());
		}
		if (obj.get("status") != null) {
			buyer.setStatus(obj.get("status").asText());
		}
		else {
			buyer.setStatus(Constants.STATUS_DEACTIVE);
		}
		if (obj.get("user") != null) {
			buyer.setUpdatedBy(obj.get("user").asText());
		} else {
			buyer.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		buyer.setUpdatedOn(Instant.now());
		buyer = buyerRepository.save(buyer);
		logger.info("Updating buyer completed" + buyer.toString());
		return buyer;
	}

	public List<Buyer> searchBuyer(@RequestParam Map<String, String> requestObj) throws NegativeIdException {
		Buyer buyer = new Buyer();
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			
			Long buyId =Long.parseLong(requestObj.get("id"));
			
			if(buyId < 0) {
				throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
			}
			buyer.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (requestObj.get("firstName") != null) {
			buyer.setFirstName(requestObj.get("firstName"));
			isFilter = true;
		}
		if (requestObj.get("lastName") != null) {
			buyer.setLastName(requestObj.get("lastName"));
			isFilter = true;
		}
		if (requestObj.get("phoneNumber") != null) {
			buyer.setPhoneNumber(requestObj.get("phoneNumber"));
			isFilter = true;
		}
		if (requestObj.get("email") != null) {
			buyer.setEmail(requestObj.get("email"));
			isFilter = true;
		}
		if (requestObj.get("address") != null) {
			buyer.setAddress(requestObj.get("address"));
			isFilter = true;
		}
		if (requestObj.get("zipCode") != null) {
			buyer.setZipCode(requestObj.get("zipCode"));
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant inst = Instant.parse(requestObj.get("createdOn"));
			buyer.setCreatedOn(inst);
			isFilter = true;
		}

		if (requestObj.get("createdBy") != null) {
			buyer.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant inst = Instant.parse(requestObj.get("updatedOn"));
			buyer.setUpdatedOn(inst);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			buyer.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}
		List<Buyer> list = null;
		if (isFilter) {
			list = this.buyerRepository.findAll(Example.of(buyer), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.buyerRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		logger.info("Buyer search completed. Total records: " + list.size());

		return list;
	}

	public void deleteBuyer(Long id) {
		buyerRepository.deleteById(id);
		logger.info("Buyer deleted successfully");
	}
}
