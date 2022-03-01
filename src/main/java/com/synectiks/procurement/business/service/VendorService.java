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
import com.synectiks.procurement.domain.Vendor;
import com.synectiks.procurement.repository.VendorRepository;

@Service
public class VendorService {

	private static final Logger logger = LoggerFactory.getLogger(VendorService.class);

	@Autowired
	private VendorRepository vendorRepository;

	public Vendor getVendor(Long id) {
		logger.info("Getting vendor by id: " + id);
		Optional<Vendor> ovn = vendorRepository.findById(id);
		if (ovn.isPresent()) {
			logger.info("Vendor: " + ovn.get().toString());
			return ovn.get();
		}
		logger.warn("Vendor not found");
		return null;
	}

	public Vendor addVendor(ObjectNode obj) {
		Vendor vendor = new Vendor();

		if (obj.get("firstName") != null) {
			vendor.setFirstName(obj.get("firstName").asText());
		}
		if (obj.get("middleName") != null) {
			vendor.setMiddleName(obj.get("middleName").asText());
		}
		if (obj.get("lastName") != null) {
			vendor.setLastName(obj.get("lastName").asText());
		}
		if (obj.get("phoneNumber") != null) {
			vendor.setPhoneNumber(obj.get("phoneNumber").asText());
		}
		if (obj.get("email") != null) {
			vendor.setEmail(obj.get("email").asText());
		}
		if (obj.get("address") != null) {
			vendor.setAddress(obj.get("address").asText());
		}
		if (obj.get("zipCode") != null) {
			vendor.setZipCode(obj.get("zipCode").asText());
		}
		if (obj.get("status") != null) {
			vendor.setStatus(obj.get("status").asText());
		}
		if (obj.get("user") != null) {
			vendor.setCreatedBy(obj.get("user").asText());
			vendor.setUpdatedBy(obj.get("user").asText());
		} else {
			vendor.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			vendor.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		vendor.setCreatedOn(now);
		vendor.setUpdatedOn(now);
		vendor = vendorRepository.save(vendor);
		logger.info("Vendor added successfully: " + vendor.toString());
		return vendor;
	}

	public Vendor updateVendor(ObjectNode obj) {
		Optional<Vendor> ur = vendorRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.error("Vendor not found");
			return null;
		}
		Vendor vendor = ur.get();

		if (obj.get("firstName") != null) {
			vendor.setFirstName(obj.get("firstName").asText());
		}
		if (obj.get("middleName") != null) {
			vendor.setMiddleName(obj.get("middleName").asText());
		}
		if (obj.get("lastName") != null) {
			vendor.setLastName(obj.get("lastName").asText());
		}
		if (obj.get("phoneNumber") != null) {
			vendor.setPhoneNumber(obj.get("phoneNumber").asText());
		}
		if (obj.get("email") != null) {
			vendor.setEmail(obj.get("email").asText());
		}
		if (obj.get("address") != null) {
			vendor.setAddress(obj.get("address").asText());
		}
		if (obj.get("zipCode") != null) {
			vendor.setZipCode(obj.get("zipCode").asText());
		}
		if (obj.get("status") != null) {
			vendor.setStatus(obj.get("status").asText());
		}
		if (obj.get("user") != null) {
			vendor.setUpdatedBy(obj.get("user").asText());
		} else {
			vendor.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		vendor.setUpdatedOn(Instant.now());
		vendor = vendorRepository.save(vendor);
		logger.info("Updating vendor completed" + vendor.toString());
		return vendor;
	}

	public List<Vendor> searchVendor(@RequestParam Map<String, String> requestObj) {
		Vendor vendor = new Vendor();
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			vendor.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (requestObj.get("firstName") != null) {
			vendor.setFirstName(requestObj.get("firstName"));
			isFilter = true;
		}
		if (requestObj.get("lastName") != null) {
			vendor.setLastName(requestObj.get("lastName"));
			isFilter = true;
		}
		if (requestObj.get("phoneNumber") != null) {
			vendor.setPhoneNumber(requestObj.get("phoneNumber"));
			isFilter = true;
		}
		if (requestObj.get("email") != null) {
			vendor.setEmail(requestObj.get("email"));
			isFilter = true;
		}
		if (requestObj.get("address") != null) {
			vendor.setAddress(requestObj.get("address"));
			isFilter = true;
		}
		if (requestObj.get("zipCode") != null) {
			vendor.setZipCode(requestObj.get("zipCode"));
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant inst = Instant.parse(requestObj.get("createdOn"));
			vendor.setCreatedOn(inst);
			isFilter = true;
		}

		if (requestObj.get("createdBy") != null) {
			vendor.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant inst = Instant.parse(requestObj.get("updatedOn"));
			vendor.setUpdatedOn(inst);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			vendor.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}
		List<Vendor> list = null;
		if (isFilter) {
			list = this.vendorRepository.findAll(Example.of(vendor), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.vendorRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		logger.info("Vendor search completed. Total records: " + list.size());

		return list;
	}

	public void deleteVender(Long id) {
		vendorRepository.deleteById(id);
		logger.info("Vendor deleted successfully");
	}
}
