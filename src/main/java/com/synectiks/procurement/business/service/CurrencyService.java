package com.synectiks.procurement.business.service;

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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Currency;
import com.synectiks.procurement.repository.CurrencyRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@Service
public class CurrencyService {
	private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

	@Autowired
	private CurrencyRepository currencyRepository;

	public Currency getCurrency(Long id) {
		logger.info("Getting currency by id: " + id);
		Optional<Currency> o = currencyRepository.findById(id);
		if (o.isPresent()) {
			logger.info("Currency: " + o.get().toString());
			return o.get();
		}
		logger.warn("Currency not found");
		return null;
	}

	public Currency addCurrency(ObjectNode obj){
		Currency currency = new Currency();

		if (obj.get("code") != null) {
			currency.setCode(obj.get("code").asText());
		}
		if (obj.get("countryName") != null) {
			currency.setCountryName(obj.get("countryName").asText());
		}
		if (obj.get("countryCode") != null) {
			currency.setCountryCode(obj.get("countryCode").asText());
		}
//		if (obj.get("symbolFilePath") != null) {
//			currency.setSymbolFilePath(obj.get("symbolFilePath").asText());
//		}

		currency = currencyRepository.save(currency);
		logger.info("Currency added successfully. " + currency.toString());
		return currency;
	}

	public Currency updateCurrency(ObjectNode obj) throws IdNotFoundException, NegativeIdException, DataNotFoundException{
		
		if(org.apache.commons.lang3.StringUtils.isBlank(obj.get("id").asText())) {
			logger.error("Currency could not be updated. Currency id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}
		
		Long curr = Long.parseLong(obj.get("id").asText());
		if( curr < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}
		
		Optional<Currency> ur = currencyRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.warn("Currency id not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}
		Currency currency = ur.get();

		if (obj.get("code") != null) {
			currency.setCode(obj.get("code").asText());
		}
		if (obj.get("countryName") != null) {
			currency.setCountryName(obj.get("countryName").asText());
		}
		if (obj.get("countryCode") != null) {
			currency.setCountryCode(obj.get("countryCode").asText());
		}
//		if (obj.get("symbolFilePath") != null) {
//			currency.setSymbolFilePath(obj.get("symbolFilePath").asText());
//		}
		currency = currencyRepository.save(currency);
		logger.info("Updating currency completed" + currency.toString());
		return currency;
	}

	public List<Currency> searchCurrency(Map<String, String> requestObj) throws NegativeIdException {
		Currency currency = new Currency();
		Long currencyId =Long.parseLong(requestObj.get("id"));
		if(currencyId < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			currency.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
//		if (requestObj.get("departmentId") != null) {
//			requisition.setDepartment(requestObj.get("departmentId"));
//			isFilter = true;
//		}

		if (requestObj.get("code") != null) {
			currency.setCode(requestObj.get("code"));
			isFilter = true;
		}
		if (requestObj.get("countryName") != null) {
			currency.setCountryName(requestObj.get("countryName"));
			isFilter = true;
		}
		if (requestObj.get("countryCode") != null) {
			currency.setCountryCode(requestObj.get("countryCode"));
			isFilter = true;
		}
//		if (requestObj.get("symbolFilePath") != null) {
//			currency.setSymbolFilePath(requestObj.get("symbolFilePath"));
//			isFilter = true;
//		}
		
		List<Currency> list = null;
		if (isFilter) {
			list = this.currencyRepository.findAll(Example.of(currency), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.currencyRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		logger.info("Currency search completed. Total records: " + list.size());
		return list;

	}

	public boolean deleteCurrency(Long id) throws IdNotFoundException, NegativeIdException, DataNotFoundException {
		if (id == null) {
			logger.error("Currency could not be deleted. Currency id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}
		if (id < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}

		Optional<Currency> currency = currencyRepository.findById(id);

		if (!currency.isPresent()) {
			logger.error("Currency could not be deleted. Currency not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}
		currencyRepository.deleteById(id);
		
		return true;

	
	}
}
