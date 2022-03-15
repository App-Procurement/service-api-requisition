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
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.repository.DepartmentRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@Service
public class DepartmentService {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

	@Autowired
	private DepartmentRepository departmentRepository;

	public Department getDepartment(Long id) {
		logger.info("Getting department by id: " + id);
		Optional<Department> o = departmentRepository.findById(id);
		if (o.isPresent()) {
			logger.info("Department: " + o.get().toString());
			return o.get();
		}
		logger.warn("Department not found");
		return null;
	}

	public Department addDepartment(ObjectNode obj) {
		Department department = new Department();
		if (obj.get("name") != null) {
			department.setName(obj.get("name").asText());
		}
		if (obj.get("status") != null) {
			department.setStatus(obj.get("status").asText());
		}
		department = departmentRepository.save(department);
		logger.info("Department added successfully. " + department.toString());
		return department;
	}

	public Department updateDepartment(ObjectNode obj)
			throws IdNotFoundException, NegativeIdException, DataNotFoundException {

		if (org.apache.commons.lang3.StringUtils.isBlank(obj.get("id").asText())) {
			logger.error("Department could not be updated. Department id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}

		Long dep = Long.parseLong(obj.get("id").asText());
		if (dep < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}

		Optional<Department> ur = departmentRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.warn("Department id not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}

		Department department = ur.get();
		if (obj.get("status") != null) {
			department.setStatus(obj.get("status").asText());
		}

		if (obj.get("name") != null) {
			department.setName(obj.get("name").asText());
		}
		department = departmentRepository.save(department);
		logger.info("Updating department completed");
		return department;
	}

	public List<Department> searchDepartment(Map<String, String> requestObj) throws NegativeIdException {
		Department department = new Department();
		boolean isFilter = false;
		if (requestObj.get("id") != null) {

			Long depId = Long.parseLong(requestObj.get("id"));

			if (depId < 0) {
				throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
			}
			department.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
		if (requestObj.get("name") != null) {
			department.setName(requestObj.get("name"));
			isFilter = true;
		}

		List<Department> list = null;
		if (isFilter) {
			list = this.departmentRepository.findAll(Example.of(department), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.departmentRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		logger.info("Department search completed. Total records: " + list.size());
		return list;

	}

	public boolean deleteDepartment(Long id) throws DataNotFoundException, NegativeIdException, IdNotFoundException {
		if (id == null) {
			logger.error("Department could not be deleted. Department id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}
		if (id < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}

		Optional<Department> department = departmentRepository.findById(id);

		if (!department.isPresent()) {
			logger.error("Department could not be deleted. Department not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}

		departmentRepository.deleteById(id);
		return true;
	}

	public List<Department> getAllDepartment() {
		List<Department> list = departmentRepository.findAll(Sort.by(Direction.ASC, "id"));
		logger.info("All department. Total records: " + list.size());
		return list;
	}
}
